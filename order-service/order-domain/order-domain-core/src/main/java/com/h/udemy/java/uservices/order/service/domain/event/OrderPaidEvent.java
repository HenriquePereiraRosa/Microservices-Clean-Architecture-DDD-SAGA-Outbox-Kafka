package com.h.udemy.java.uservices.order.service.domain.event;

import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {

    private final DomainEventPublisher<OrderPaidEvent> paidEventPublisher;

    public OrderPaidEvent(Order order,
                          ZonedDateTime createdAt,
                          DomainEventPublisher<OrderPaidEvent> paidEventPublisher) {
        super(order, createdAt);
        this.paidEventPublisher = paidEventPublisher;

    }

    @Override
    public void fire() {
        paidEventPublisher.publish(this);
    }
}
