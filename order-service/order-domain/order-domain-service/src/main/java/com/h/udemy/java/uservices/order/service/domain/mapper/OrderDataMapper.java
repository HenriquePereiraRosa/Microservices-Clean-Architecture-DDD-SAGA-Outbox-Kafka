package com.h.udemy.java.uservices.order.service.domain.mapper;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.PaymentOrderStatus;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantOrderStatus;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.dto.create.OrderAddressDTO;
import com.h.udemy.java.uservices.order.service.domain.dto.create.OrderItemDTO;
import com.h.udemy.java.uservices.order.service.domain.dto.message.CustomerModel;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.entity.*;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCancelledEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalEventProduct;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
                .products(createOrderCommand.items().stream()
                        .map(orderItem -> new Product(orderItem.productId()))
                        .collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(createOrderCommand.customerId())
                .restaurantId(createOrderCommand.restaurantId())
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.address()))
                .price(createOrderCommand.price())
                .items(orderItemsToOrderItemsEntities(createOrderCommand.items()))
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemsEntities(List<OrderItemDTO> itemsDTO) {
        return itemsDTO.stream().map(orderItemDTO ->
                        OrderItem.builder()
                                .product(orderItemDTO.productId())
                                .price(orderItemDTO.price())
                                .quantity(orderItemDTO.quantity())
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

    public OrderPaymentEventPayload orderCreatedEventToOrderPaymentEventPayload(OrderCreatedEvent orderCreatedEvent) {
        return OrderPaymentEventPayload.builder()
                .customerId(orderCreatedEvent.getOrder().getCustomerId().getValue().toString())
                .orderId(orderCreatedEvent.getOrder().getId().getValue().toString())
                .price(orderCreatedEvent.getOrder().getPrice().getAmount())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .createdAt(orderCreatedEvent.getCreatedAt())
                .build();
    }

    public OrderPaymentEventPayload orderCancelledEventToOrderPaymentEventPayload(OrderCancelledEvent orderCancelledEvent) {
        return OrderPaymentEventPayload.builder()
                .customerId(orderCancelledEvent.getOrder().getCustomerId().getValue().toString())
                .orderId(orderCancelledEvent.getOrder().getId().getValue().toString())
                .price(orderCancelledEvent.getOrder().getPrice().getAmount())
                .createdAt(orderCancelledEvent.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.CANCELLED.name())
                .build();
    }

    public OrderApprovalEventPayload orderPaidEventToOrderApprovalEventPayload(OrderPaidEvent orderPaidEvent) {
        return OrderApprovalEventPayload.builder()
                .orderId(orderPaidEvent.getOrder().getId().getValue().toString())
                .restaurantId(orderPaidEvent.getOrder().getRestaurantId().getValue().toString())
                .restaurantOrderStatus(RestaurantOrderStatus.PAID.name())
                .products(orderPaidEvent.getOrder().getItems().stream().map(orderItem ->
                        OrderApprovalEventProduct.builder()
                                .id(orderItem.getProduct().getId().getValue().toString())
                                .quantity(orderItem.getQuantity())
                                .build()).collect(Collectors.toList()))
                .price(orderPaidEvent.getOrder().getPrice().getAmount())
                .createdAt(orderPaidEvent.getCreatedAt())
                .build();
    }

    public Customer customerModelToCustomer(CustomerModel customerModel) {
        return new Customer(
                new CustomerId(UUID.fromString(customerModel.getId())),
                customerModel.getUsername(),
                customerModel.getFirstName(),
                customerModel.getLastName());
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
