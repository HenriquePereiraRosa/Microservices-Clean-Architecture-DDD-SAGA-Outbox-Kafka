package com.h.udemy.java.uservices.order.service.domain.event.publisher;

import com.h.udemy.java.uservices.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
