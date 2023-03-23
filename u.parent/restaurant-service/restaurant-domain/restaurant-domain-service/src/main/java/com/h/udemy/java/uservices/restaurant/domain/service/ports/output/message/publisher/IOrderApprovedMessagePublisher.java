package com.h.udemy.java.uservices.restaurant.domain.service.ports.output.message.publisher;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderApprovedEvent;

public interface IOrderApprovedMessagePublisher extends IDomainEventPublisher<OrderApprovedEvent> {


}
