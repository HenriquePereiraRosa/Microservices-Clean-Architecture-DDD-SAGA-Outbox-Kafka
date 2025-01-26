package com.h.udemy.java.uservices.domain.event;

public final class EmptyEvent implements DomainEvent {

    public static final EmptyEvent INSTANCE = new EmptyEvent();

    private EmptyEvent() {}
}
