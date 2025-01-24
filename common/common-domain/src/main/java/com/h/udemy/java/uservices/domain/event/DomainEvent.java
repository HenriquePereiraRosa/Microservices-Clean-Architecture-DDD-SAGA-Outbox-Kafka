package com.h.udemy.java.uservices.domain.event;

public interface DomainEvent<T> {
    void fire();
}
