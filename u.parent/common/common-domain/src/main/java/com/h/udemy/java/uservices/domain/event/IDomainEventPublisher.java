package com.h.udemy.java.uservices.domain.event;

public interface IDomainEventPublisher<T extends IDomainEvent> {

    void publish(T domainEvent);

}
