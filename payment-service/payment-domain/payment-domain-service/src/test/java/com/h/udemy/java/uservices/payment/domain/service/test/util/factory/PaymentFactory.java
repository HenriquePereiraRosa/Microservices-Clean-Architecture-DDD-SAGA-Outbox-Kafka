package com.h.udemy.java.uservices.payment.domain.service.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.PaymentId;

import java.math.BigDecimal;
import java.util.UUID;

import static com.h.udemy.java.uservices.constants.TestConstants.*;

public class PaymentFactory {
    private static final double PRICE_VALUE = 102.22;

    static public Payment mockPayment() {

        return Payment.builder()
                .paymentId(new PaymentId(PAYMENT_ID))
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(CUSTOMER_ID))
                .price(new Money(new BigDecimal(PRICE_VALUE)))
                .build();
    }

    static public Payment mockPayment(double aPrice) {

        return Payment.builder()
                .paymentId(new PaymentId(PAYMENT_ID))
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(CUSTOMER_ID))
                .price(new Money(new BigDecimal(aPrice)))
                .build();
    }

    static public Payment mockPayment(UUID aCustomerId) {

        return Payment.builder()
                .paymentId(new PaymentId(PAYMENT_ID))
                .orderId(new OrderId(ORDER_ID))
                .customerId(new CustomerId(aCustomerId))
                .price(new Money(new BigDecimal(PRICE_VALUE)))
                .build();
    }
}
