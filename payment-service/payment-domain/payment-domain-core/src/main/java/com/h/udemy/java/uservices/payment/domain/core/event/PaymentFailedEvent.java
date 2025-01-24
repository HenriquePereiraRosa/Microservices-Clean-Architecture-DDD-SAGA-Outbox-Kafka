package com.h.udemy.java.uservices.payment.domain.core.event;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentFailedEvent> eventPublisher;

    public PaymentFailedEvent(Payment payment,
                              ZonedDateTime createdAt,
                              List<String> failureMessages,
                              DomainEventPublisher<PaymentFailedEvent> eventPublisher) {
        super(payment, createdAt, failureMessages);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void fire() {
        eventPublisher.publish(this);
    }
}
