package com.h.udemy.java.uservices.restaurant.domain.core.entity;

import com.h.udemy.java.uservices.domain.entity.AggregateRoot;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderApprovalStatus;
import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.restaurant.domain.core.valueobject.OrderApprovalId;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.RESTAURANT_PAYMENT_NOT_COMPLETED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.RESTAURANT_PRODUCT_NOT_AVAILABLE;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.RESTAURANT_PRODUCT_PRICE_INCORRECT;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {

    private OrderApproval orderApproval;
    @Setter
    private boolean active;
    private final OrderDetail orderDetail;

    private Restaurant(Builder builder) {
        setId(builder.restaurantId);
        orderApproval = builder.orderApproval;
        active = builder.active;
        orderDetail = builder.orderDetail;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private OrderApproval orderApproval;
        private boolean active;
        private OrderDetail orderDetail;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder orderApproval(OrderApproval val) {
            orderApproval = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }


    public void validateOrder(List<String> failureMessages) {
        if(orderDetail.getOrderStatus() != OrderStatus.PAID) {
            failureMessages.add(RESTAURANT_PAYMENT_NOT_COMPLETED
                    .build(orderDetail.getId()));
        }

        Money totalAmount = orderDetail.getProducts().stream().map(product -> {
            if(!product.isAvailable()) {
                failureMessages.add(RESTAURANT_PRODUCT_NOT_AVAILABLE
                        .build(product.getId().getValue()));
            }
            return product.getPrice().multiply(product.getQuantity());

        }).reduce(Money.ZERO, Money::add);

        if(!totalAmount.equals(orderDetail.getTotalAmount())) {
            failureMessages.add(RESTAURANT_PRODUCT_PRICE_INCORRECT
                    .build(orderDetail.getId()));
        }
    }

    public void constructOrderApproval(OrderApprovalStatus approvalStatus) {
        this.orderApproval = OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
                .restaurantId(this.getId())
                .orderId(this.getOrderDetail().getId())
                .approvalStatus(approvalStatus)
                .build();
    }
}