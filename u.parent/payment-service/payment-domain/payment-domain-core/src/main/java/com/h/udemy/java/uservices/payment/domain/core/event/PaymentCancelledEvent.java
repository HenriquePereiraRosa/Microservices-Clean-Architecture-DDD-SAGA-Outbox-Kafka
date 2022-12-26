package com.h.udemy.java.uservices.payment.domain.core.event;

import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentCancelledEvent extends PaymentEvent {

    public PaymentCancelledEvent(Payment payment,
                                 ZonedDateTime createdAt,
                                 List<String> failureMessages) {
        super(payment, createdAt, failureMessages);
    }
}
