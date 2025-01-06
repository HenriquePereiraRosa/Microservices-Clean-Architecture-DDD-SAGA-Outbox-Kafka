package com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;

public interface IOrderPaidRestaurantRequestMessagePublisher
        extends IDomainEventPublisher<OrderPaidEvent> {
}
