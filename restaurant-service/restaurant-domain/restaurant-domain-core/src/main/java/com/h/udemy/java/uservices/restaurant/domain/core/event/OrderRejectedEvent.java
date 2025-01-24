package com.h.udemy.java.uservices.restaurant.domain.core.event;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderRejectedEvent> eventPublisher;
    public OrderRejectedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt,
                              DomainEventPublisher<OrderRejectedEvent> eventPublisher) {

        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void fire() {
        eventPublisher.publish(this);
    }
}
