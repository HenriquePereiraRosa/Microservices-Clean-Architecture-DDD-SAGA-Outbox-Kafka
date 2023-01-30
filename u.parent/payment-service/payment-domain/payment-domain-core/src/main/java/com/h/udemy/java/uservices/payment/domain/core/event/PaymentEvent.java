package com.h.udemy.java.uservices.payment.domain.core.event;

import com.h.udemy.java.uservices.domain.event.IDomainEvent;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class PaymentEvent implements IDomainEvent<Payment> {

    private final Payment payment;
    private final ZonedDateTime createdAt;
    private final List<String> failureMessages;

    public PaymentEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
        this.payment = payment;
        this.createdAt = createdAt;
        this.failureMessages = failureMessages;
    }

    public Payment getPayment() {
        return payment;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }
}
