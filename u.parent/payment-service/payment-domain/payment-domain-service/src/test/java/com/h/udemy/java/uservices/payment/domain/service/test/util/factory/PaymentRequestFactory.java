package com.h.udemy.java.uservices.payment.domain.service.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;

import java.math.BigDecimal;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.test.constants.ConstantsTest.CUSTOMER_UUID;
import static com.h.udemy.java.uservices.domain.test.constants.ConstantsTest.ORDER_UUID;


public class PaymentRequestFactory {
    private static final double PRICE_VALUE = 102.22;
    static public PaymentRequest createPaymentRequest(double price) {

        return PaymentRequest.builder()
                .orderId(ORDER_UUID.toString())
                .customerId(CUSTOMER_UUID.toString())
                .price(new BigDecimal(price))
                .build();
    }

    static public PaymentRequest createPaymentRequest() {

        return PaymentRequest.builder()
                .orderId(ORDER_UUID.toString())
                .customerId(CUSTOMER_UUID.toString())
                .price(new BigDecimal(PRICE_VALUE))
                .build();
    }
    static public Payment createPaymentRequest(UUID customerId) {

        return Payment.builder()
                .orderId(new OrderId(ORDER_UUID))
                .customerId(new CustomerId(customerId))
                .price(new Money(new BigDecimal(PRICE_VALUE)))
                .build();
    }
}
