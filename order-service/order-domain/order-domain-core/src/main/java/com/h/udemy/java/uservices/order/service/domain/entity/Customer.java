package com.h.udemy.java.uservices.order.service.domain.entity;

import com.h.udemy.java.uservices.domain.entity.BaseEntity;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;

import java.util.UUID;

public class Customer extends BaseEntity<CustomerId> {
    public Customer(){}


    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
    public Customer(UUID customerId) {
        super.setId(new CustomerId(customerId));
    }
}
