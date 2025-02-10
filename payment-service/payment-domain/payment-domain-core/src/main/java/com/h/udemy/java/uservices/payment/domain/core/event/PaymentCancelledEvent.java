package com.h.udemy.java.uservices.payment.domain.core.event;

import java.time.ZonedDateTime;
import java.util.Collections;

import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;

public class PaymentCancelledEvent extends PaymentEvent {

    public PaymentCancelledEvent(Payment payment,
                                 ZonedDateTime createdAt) {

        super(payment, createdAt, Collections.emptyList());
    }
}
