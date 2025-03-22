package com.h.udemy.java.uservices.core.entity;

import com.h.udemy.java.uservices.domain.entity.AggregateRoot;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import lombok.Getter;

@Getter
public class Customer extends AggregateRoot<CustomerId> {
    private final String username;
    private final String firstName;
    private final String lastName;

    public Customer(
            CustomerId customerId,
            String username,
            String firstName,
            String lastName) {

        super.setId(customerId);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}


