package com.h.udemy.java.uservices.restaurant.domain.service.ports.output.message.publisher;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderApprovedEvent;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {


}
