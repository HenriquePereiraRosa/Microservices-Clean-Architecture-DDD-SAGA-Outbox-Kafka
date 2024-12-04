package com.h.udemy.java.uservices.order.service.domain.event;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    private final IDomainEventPublisher<OrderCreatedEvent> createdEventPublisher;
    public OrderCreatedEvent(Order order,
                             ZonedDateTime createdAt,
                             IDomainEventPublisher<OrderCreatedEvent> createdEventPublisher) {
        super(order, createdAt);
        this.createdEventPublisher = createdEventPublisher;
    }

    @Override
    public void fire() {
        createdEventPublisher.publish(this);
    }
}
