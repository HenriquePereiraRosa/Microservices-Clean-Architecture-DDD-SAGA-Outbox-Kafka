package com.h.udemy.java.uservices.order.service.domain.event;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCancelledEvent> cancelledEventPublisher;

    public OrderCancelledEvent(Order order,
                               ZonedDateTime createdAt,
                               DomainEventPublisher<OrderCancelledEvent> cancelledEventPublisher) {
        super(order, createdAt);
        this.cancelledEventPublisher = cancelledEventPublisher;
    }

    @Override
    public void fire() {
        cancelledEventPublisher.publish(this);
    }
}
