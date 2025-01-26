package com.h.udemy.java.uservices.saga;

import com.h.udemy.java.uservices.domain.event.DomainEvent;

public interface SagaStep<T> {

    void process(T data);
    void rollback(T data);

}