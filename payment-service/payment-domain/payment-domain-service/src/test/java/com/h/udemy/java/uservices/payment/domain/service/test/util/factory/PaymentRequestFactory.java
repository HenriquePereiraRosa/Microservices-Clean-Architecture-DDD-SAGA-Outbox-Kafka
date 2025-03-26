package com.h.udemy.java.uservices.payment.domain.service.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;

import java.math.BigDecimal;
import java.util.UUID;

import static com.h.udemy.java.uservices.constants.TestConstants.*;

public class PaymentRequestFactory {
    private static final double PRICE_VALUE = 102.22;
    static public PaymentRequest mockPaymentRequest(double price) {

        return PaymentRequest.builder()
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(new BigDecimal(price))
                .build();
    }

    static public PaymentRequest mockPaymentRequest() {

        return PaymentRequest.builder()
                .sagaId(SAGA_ID.toString())
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(new BigDecimal(PRICE_VALUE))
                .build();
    }
    static public Payment mockPaymentRequest(UUID customerId) {

        return Payment.builder()
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(customerId))
                .price(new Money(new BigDecimal(PRICE_VALUE)))
                .build();
    }
}
