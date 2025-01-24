package com.h.udemy.java.uservices.domain.event;

public final class EmptyEvent implements DomainEvent<Void> {

    public static final EmptyEvent INSTANCE = new EmptyEvent();

    private EmptyEvent() {}

    @Override
    public void fire() {}
}
