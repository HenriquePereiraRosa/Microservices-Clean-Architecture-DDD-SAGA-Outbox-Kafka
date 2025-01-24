package com.h.udemy.java.uservices.order.service.domain.event;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCreatedEvent> createdEventPublisher;
    public OrderCreatedEvent(Order order,
                             ZonedDateTime createdAt,
                             DomainEventPublisher<OrderCreatedEvent> createdEventPublisher) {
        super(order, createdAt);
        this.createdEventPublisher = createdEventPublisher;
    }

    @Override
    public void fire() {
        createdEventPublisher.publish(this);
    }
}
