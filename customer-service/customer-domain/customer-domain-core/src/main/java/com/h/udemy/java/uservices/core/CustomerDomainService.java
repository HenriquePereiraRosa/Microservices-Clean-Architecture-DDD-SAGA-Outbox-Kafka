package com.h.udemy.java.uservices.core;

import com.h.udemy.java.uservices.core.entity.Customer;
import com.h.udemy.java.uservices.core.event.CustomerCreatedEvent;

public interface CustomerDomainService {

    CustomerCreatedEvent validateAndInitiateCustomer(Customer customer);

}
