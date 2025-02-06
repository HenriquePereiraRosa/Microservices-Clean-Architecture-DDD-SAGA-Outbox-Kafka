package com.h.udemy.java.uservices.payment.service.messaging.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;

import java.math.BigDecimal;
import java.util.UUID;

import static com.h.udemy.java.uservices.constants.TestConstants.CUSTOMER_ID;
import static com.h.udemy.java.uservices.constants.TestConstants.ORDER_ID;


public class PaymentRequestFactory {
    private static final double PRICE_VALUE = 102.22;
    static public PaymentRequest createPaymentRequest(double price) {

        return PaymentRequest.builder()
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(new BigDecimal(price))
                .build();
    }

    static public PaymentRequest createPaymentRequest() {

        return PaymentRequest.builder()
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(new BigDecimal(PRICE_VALUE))
                .build();
    }
    static public Payment createPaymentRequest(UUID customerId) {

        return Payment.builder()
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(customerId))
                .price(new Money(new BigDecimal(PRICE_VALUE)))
                .build();
    }
}
