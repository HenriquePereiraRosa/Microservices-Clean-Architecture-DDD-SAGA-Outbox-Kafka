package com.h.udemy.java.uservices.order.service.domain.entity;

import com.h.udemy.java.uservices.domain.entity.BaseEntity;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Customer extends BaseEntity<CustomerId> {

    private String username;
    private String firstName;
    private String lastName;

    public Customer() {}

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }

    public Customer(UUID customerId) {
        super.setId(new CustomerId(customerId));
    }

    public Customer(
            CustomerId customerId,
            String username,
            String firstname,
            String lastName) {

        super.setId(customerId);
        this.username = username;
        this.firstName = firstname;
        this.lastName = lastName;
    }
}
