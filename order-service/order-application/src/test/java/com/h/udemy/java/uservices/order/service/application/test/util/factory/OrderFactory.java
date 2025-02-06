package com.h.udemy.java.uservices.order.service.application.test.util.factory;

import com.h.udemy.java.uservices.constants.TestConstants;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.OrderAddressDTO;
import com.h.udemy.java.uservices.order.service.domain.dto.create.OrderItemDTO;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.OrderItem;
import com.h.udemy.java.uservices.order.service.domain.entity.Product;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCreatedEvent;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.valueobject.OrderItemId;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.Constants.getZonedDateTimeNow;
import static com.h.udemy.java.uservices.order.service.application.test.util.OrderTestConstants.*;
import static com.h.udemy.java.uservices.saga.order.SagaConstants.ORDER_SAGA_NAME;

public class OrderFactory {

    public static CreateOrderCommand createCreateOrderCommand() {

        OrderAddressDTO orderAddressDTO = new OrderAddressDTO("sweet street",
                "01234-99",
                "Tokio");

        OrderItemDTO item = OrderItemDTO.builder()
                .productId(PRODUCT_ID.getValue())
                .price(new BigDecimal("10.99"))
                .quantity(5)
                .build();

        return CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID.getValue())
                .restaurantId(RESTAURANT_ID.getValue())
                .address(orderAddressDTO)
                .price(new BigDecimal("54.95"))
                .items(Collections.singletonList(item))
                .build();
    }

    public static Order createOrderSaved() {

        StreetAddress address = new StreetAddress(STREET_ADDRESS_ID,
                "sweet street",
                "01234-99",
                "Tokio");

        OrderItem item = OrderItem.builder()
                .orderItemId(new OrderItemId(112L))
                .product(new Product(PRODUCT_ID,
                        "product name",
                        new Money(new BigDecimal("10.99"))))
                .price(new Money(new BigDecimal("10.99")))
                .quantity(5)
                .build();

        return Order.builder()
                .orderId(ORDER_ID)
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .deliveryAddress(address)
                .price(new Money(new BigDecimal("54.95")))
                .items(List.of(item))
                .trackingId(TRACKING_ID)
                .build();
    }

    public static OrderCreatedEvent createOrderCreatedEvent(Order order) {
        return new OrderCreatedEvent(order, getZonedDateTimeNow());
    }

    public static OrderPaymentOutboxMessage createOrderPaymentOutboxMessage(){
        return OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(UUID.randomUUID())
                .createdAt(getZonedDateTimeNow())
                .type(ORDER_SAGA_NAME)
//                .payload(createPaylo paymentOutboxEntity.getPayload())
                .orderStatus(OrderStatus.APPROVED)
                .sagaStatus(SagaStatus.COMPENSATING)
                .outboxStatus(OutboxStatus.STARTED)
                .version(TestConstants.VERSION)
                .build();
    }

}
