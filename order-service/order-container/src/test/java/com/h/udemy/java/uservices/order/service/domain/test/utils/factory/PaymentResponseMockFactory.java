package com.h.udemy.java.uservices.order.service.domain.test.utils.factory;

import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.order.service.domain.dto.message.PaymentResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import static com.h.udemy.java.uservices.constants.TestConstants.PAYMENT_ID;
import static com.h.udemy.java.uservices.constants.TestConstants.PRICE;


public class PaymentResponseMockFactory {

    private static final UUID ORDER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb17");
    private static final UUID CUSTOMER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb41");

    private PaymentResponseMockFactory() {}

    static public PaymentResponse mockPaymentResponse(UUID sagaId) {

        return PaymentResponse.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(sagaId.toString())
                .paymentStatus(PaymentStatus.COMPLETED)
                .paymentId(String.valueOf(PAYMENT_ID))
                .orderId(String.valueOf(ORDER_ID))
                .customerId(CUSTOMER_ID.toString())
                .price(PRICE)
                .createdAt(Instant.now())
                .failureMessages(new ArrayList<>())
                .build();
    }
}
