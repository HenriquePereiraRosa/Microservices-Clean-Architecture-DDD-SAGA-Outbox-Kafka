package com.h.udemy.java.uservices.entity;

import com.h.udemy.java.uservices.domain.entity.AggregateRoot;
import com.h.udemy.java.uservices.domain.valueobject.*;
import com.h.udemy.java.uservices.exception.OrderDomainException;
import com.h.udemy.java.uservices.exception.OrderDomainInitialStateException;
import com.h.udemy.java.uservices.exception.OrderDomainTotalPriceException;
import com.h.udemy.java.uservices.exception.msg.I18n;
import com.h.udemy.java.uservices.valueobject.OrderItemId;
import com.h.udemy.java.uservices.valueobject.StreetAddress;
import com.h.udemy.java.uservices.valueobject.TrackingId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    @Autowired
    MessageSource messageSource;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;

        if (builder.items == null) {
            items = new ArrayList<>();
        } else {
            items = builder.items;
        }

        if (builder.failureMessages == null) {
            failureMessages = new ArrayList<>();
        } else {
            failureMessages = builder.failureMessages;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public void initialeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    private void initializeOrderItems() {
        long itemIdCounter = 1;
        for (OrderItem orderItem : items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemIdCounter++));
        }
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateInitialOrder() {
        if (orderStatus == null
                || orderStatus != OrderStatus.PENDING
                || getId() == null) {
            throw new OrderDomainInitialStateException();
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainTotalPriceException();
        }
    }

    private void validateItemsPrice() {
        final Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubtotal();
        }).reduce(Money.ZERO, Money::add);

        if (!orderItemsTotal.equals(price)) {
            throw new OrderDomainException(I18n.ERR_ORDER_TOTAL_AND_ORDER_PRICES_DIFF.getMsg()
                    + " :  (price)" + price.getAmount()
                    + " != "
                    + " (orderItemsTotal)" + orderItemsTotal.getAmount());
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException(I18n.ERR_ORDER_ITEM_PRICE_INVALID.getMsg()
                    + ": " + price.getAmount());
        }
    }

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException(I18n.ERR_ORDER_NOT_CORRECT_STATE_F_PAY.getMsg());
        }

        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException(I18n.ERR_ORDER_NOT_CORRECT_STATE_F_APPROVE.getMsg());
        }

        orderStatus = OrderStatus.APPROVED;
    }


    // Inner builder
    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
