package com.h.udemy.java.uservices.payment.domain.core.event;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCompletedEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentCompletedEvent> eventPublisher;
    public PaymentCompletedEvent(Payment payment,
                                 ZonedDateTime createdAt,
                                 DomainEventPublisher<PaymentCompletedEvent> eventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void fire() {
        eventPublisher.publish(this);
    }
}
