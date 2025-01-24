package com.h.udemy.java.uservices.domain.event;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);

}
