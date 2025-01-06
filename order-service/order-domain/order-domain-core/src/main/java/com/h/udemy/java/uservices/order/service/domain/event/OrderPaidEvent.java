package com.h.udemy.java.uservices.order.service.domain.event;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {

    private final IDomainEventPublisher<OrderPaidEvent> paidEventPublisher;

    public OrderPaidEvent(Order order,
                          ZonedDateTime createdAt,
                          IDomainEventPublisher<OrderPaidEvent> paidEventPublisher) {
        super(order, createdAt);
        this.paidEventPublisher = paidEventPublisher;

    }

    @Override
    public void fire() {
        paidEventPublisher.publish(this);
    }
}
