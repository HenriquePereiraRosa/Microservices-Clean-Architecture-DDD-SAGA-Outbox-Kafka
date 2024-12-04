package com.h.udemy.java.uservices.payment.domain.core.event;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCancelledEvent extends PaymentEvent {

    private final IDomainEventPublisher<PaymentCancelledEvent> eventPublisher;

    public PaymentCancelledEvent(Payment payment,
                                 ZonedDateTime createdAt,
                                 IDomainEventPublisher<PaymentCancelledEvent> eventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void fire() {
        eventPublisher.publish(this);
    }
}
