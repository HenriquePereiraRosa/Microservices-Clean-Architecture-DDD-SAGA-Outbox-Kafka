package com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;

public interface IOrderPaidRestaurantRequestRequestMessagePublisher
        extends IDomainEventPublisher<OrderPaidEvent> {


}
