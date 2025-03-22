package com.h.udemy.java.uservices.core.event;

import com.h.udemy.java.uservices.core.entity.Customer;
import com.h.udemy.java.uservices.domain.event.DomainEvent;
import lombok.Getter;

import java.time.ZonedDateTime;

public class CustomerCreatedEvent implements DomainEvent<Customer> {

    @Getter
    private final Customer customer;

    private final ZonedDateTime createdAt;

    public CustomerCreatedEvent(Customer customer, ZonedDateTime createdAt) {
        this.customer = customer;
        this.createdAt = createdAt;
    }

}
