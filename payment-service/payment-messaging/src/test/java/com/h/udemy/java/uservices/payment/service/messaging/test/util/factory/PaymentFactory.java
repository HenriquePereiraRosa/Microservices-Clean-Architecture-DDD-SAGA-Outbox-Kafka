package com.h.udemy.java.uservices.payment.service.messaging.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;

import java.math.BigDecimal;
import java.util.UUID;

import static com.h.udemy.java.uservices.constants.TestConstants.CUSTOMER_ID;
import static com.h.udemy.java.uservices.constants.TestConstants.ORDER_ID;


public class PaymentFactory {
    private static final double PRICE_VALUE = 102.22;

    static public Payment createPayment() {

        return Payment.builder()
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(CUSTOMER_ID))
                .price(new Money(new BigDecimal(PRICE_VALUE)))
                .build();
    }

    static public Payment createPayment(double aPrice) {

        return Payment.builder()
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(CUSTOMER_ID))
                .price(new Money(new BigDecimal(aPrice)))
                .build();
    }

    static public Payment createPayment(UUID aCustomerId) {

        return Payment.builder()
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(aCustomerId))
                .price(new Money(new BigDecimal(PRICE_VALUE)))
                .build();
    }
}
