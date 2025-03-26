package com.h.udemy.java.uservices.payment.domain.service.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.payment.domain.service.outbox.model.OrderEventPayload;
import com.h.udemy.java.uservices.payment.domain.service.outbox.model.OrderOutboxMessage;

import java.util.Collections;
import java.util.UUID;

import static com.h.udemy.java.uservices.constants.TestConstants.*;
import static com.h.udemy.java.uservices.domain.Constants.getZonedDateTimeNow;
import static com.h.udemy.java.uservices.saga.order.SagaConstants.ORDER_SAGA_NAME;

public class OrderOutboxMessageMockFactory {

    static public OrderOutboxMessage mockOrderOutboxMessage() {

        final OrderEventPayload orderEventPayload = OrderEventPayload.builder()
                .paymentId(String.valueOf(PAYMENT_ID))
                .customerId(String.valueOf(CUSTOMER_ID))
                .orderId(String.valueOf(ORDER_ID))
                .price(PRICE)
                .createdAt(getZonedDateTimeNow())
                .paymentStatus(PaymentStatus.COMPLETED.name())
                .failureMessages(Collections.emptyList())
                .build();

        return OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(SAGA_ID)
                .createdAt(getZonedDateTimeNow())
                .processedAt(getZonedDateTimeNow())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderEventPayload))
                .outboxStatus(OutboxStatus.STARTED)
                .build();
    }

    private static String createPayload(OrderEventPayload orderEventPayload) {
            return orderEventPayload.toString();
    }
}
