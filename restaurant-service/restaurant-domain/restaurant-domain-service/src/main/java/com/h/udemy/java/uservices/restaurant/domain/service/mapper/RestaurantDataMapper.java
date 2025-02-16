package com.h.udemy.java.uservices.restaurant.domain.service.mapper;

import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.OrderDetail;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.Restaurant;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.RestaurantProduct;
import com.h.udemy.java.uservices.restaurant.domain.service.dto.RestaurantApprovalRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataMapper {
    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest approvalRequest) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(UUID.fromString(approvalRequest.getRestaurantId())))
                .orderDetail(OrderDetail.builder()
                        .orderId(new OrderId(UUID.fromString(approvalRequest.getOrderId())))
                        .products(approvalRequest
                                .getProducts()
                                .stream()
                                .map(restaurantProduct -> RestaurantProduct.builder()
                                        .productId(restaurantProduct.getId())
                                        .quantity(restaurantProduct.getQuantity())
                                        .build())
                                .collect(Collectors.toList()))
                        .totalAmount(new Money(approvalRequest.getPrice()))
                        .orderStatus(OrderStatus.valueOf(approvalRequest.getRestaurantOrderStatus().name()))
                        .build())
                .build();
    }
}
