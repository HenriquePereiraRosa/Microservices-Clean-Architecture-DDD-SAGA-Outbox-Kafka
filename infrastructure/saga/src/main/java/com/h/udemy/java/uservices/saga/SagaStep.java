package com.h.udemy.java.uservices.saga;

import com.h.udemy.java.uservices.domain.event.DomainEvent;

public interface SagaStep<T, S extends DomainEvent, U extends DomainEvent> {

    S process(T data);
    U rollback(T data);

}