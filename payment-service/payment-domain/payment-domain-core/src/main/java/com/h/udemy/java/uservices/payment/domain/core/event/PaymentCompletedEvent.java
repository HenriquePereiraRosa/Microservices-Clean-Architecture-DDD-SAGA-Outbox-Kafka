package com.h.udemy.java.uservices.payment.domain.core.event;

import java.time.ZonedDateTime;
import java.util.Collections;

import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;

public class PaymentCompletedEvent extends PaymentEvent {

    public PaymentCompletedEvent(Payment payment,
                                 ZonedDateTime createdAt) {
        super(payment, createdAt, Collections.emptyList());
    }
}
