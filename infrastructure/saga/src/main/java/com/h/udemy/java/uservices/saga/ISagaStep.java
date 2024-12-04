package com.h.udemy.java.uservices.saga;

import com.h.udemy.java.uservices.domain.event.IDomainEvent;

public interface ISagaStep<T, S extends IDomainEvent, U extends IDomainEvent> {

    S process(T data);
    U rollback(T data);

}