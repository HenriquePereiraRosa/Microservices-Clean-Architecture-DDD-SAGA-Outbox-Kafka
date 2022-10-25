package com.h.udemy.java.uservices.domain.event;

public interface DomainPublisherEvent<T extends DomainEvent> {

    void publish(T domainEvent);

}
