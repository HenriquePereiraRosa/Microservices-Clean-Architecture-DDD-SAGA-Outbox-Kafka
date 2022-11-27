package com.h.udemy.java.uservices.order.service.domain.mapper;

import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.dto.create.OrderAddressDTO;
import com.h.udemy.java.uservices.order.service.domain.dto.create.OrderItemDTO;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.OrderItem;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.entity.Restaurant;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream()
                        .map(orderItem -> new Product(orderItem.getProductId()))
                        .collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(createOrderCommand.getCustomerId())
                .restaurantId(createOrderCommand.getRestaurantId())
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(createOrderCommand.getPrice())
                .items(orderItemsToOrderItemsEntities(createOrderCommand.getItems()))
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemsEntities(List<OrderItemDTO> itemsDTO) {
        return itemsDTO.stream().map(orderItemDTO ->
                        OrderItem.builder()
                                .product(orderItemDTO.getProductId())
                                .price(orderItemDTO.getPrice())
                                .quantity(orderItemDTO.getQuantity())
                                .build())
                .collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddressDTO address) {
        return new StreetAddress(
                UUID.randomUUID(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity()
        );
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderStatus(order.getOrderStatus())
                .trackingId(order.getTrackingId().getValue())
                .message(message)
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    public List<TrackOrderResponse> ordersToTrackOrderResponse(List<Order> orders) {
        return orders.stream().map(order ->
                TrackOrderResponse.builder()
                        .orderTrackingId(order.getTrackingId().getValue())
                        .orderStatus(order.getOrderStatus())
                        .failureMessages(order.getFailureMessages())
                        .build())
                .collect(Collectors.toList());
    }
}
