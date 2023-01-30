package com.h.udemy.java.uservices.order.service.domain.event.publisher;

import com.h.udemy.java.uservices.domain.event.IDomainEvent;

public interface DomainEventPublisher<T extends IDomainEvent> {

    void publish(T domainEvent);
}
