package com.h.udemy.java.uservices.payment.domain.core.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;

import java.math.BigDecimal;
import java.util.UUID;

import static com.h.udemy.java.uservices.payment.domain.core.test.Const.ORDER_ID;

public class PaymentFactory {
    private static final double PRICE_VALUE = 102.22;
    static public Payment createPayment(double price) {

        return Payment.builder()
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(UUID.randomUUID()))
                .price(new Money(new BigDecimal(price)))
                .build();
    }

    static public Payment createPayment() {

        return Payment.builder()
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(UUID.randomUUID()))
                .price(new Money(new BigDecimal(PRICE_VALUE)))
                .build();
    }
    static public Payment createPayment(UUID customerId) {

        return Payment.builder()
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(customerId))
                .price(new Money(new BigDecimal(PRICE_VALUE)))
                .build();
    }
}
