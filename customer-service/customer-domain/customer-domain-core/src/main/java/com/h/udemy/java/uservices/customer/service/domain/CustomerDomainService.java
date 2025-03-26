package com.h.udemy.java.uservices.customer.service.domain;

import com.h.udemy.java.uservices.customer.service.domain.entity.Customer;
import com.h.udemy.java.uservices.customer.service.domain.event.CustomerCreatedEvent;

public interface CustomerDomainService {

    CustomerCreatedEvent validateAndInitiateCustomer(Customer customer);

}
