package com.h.udemy.java.uservices.service.ports.output.repository;

import com.h.udemy.java.uservices.core.entity.Customer;

public interface CustomerRepository {

    Customer createCustomer(Customer customer);
}
