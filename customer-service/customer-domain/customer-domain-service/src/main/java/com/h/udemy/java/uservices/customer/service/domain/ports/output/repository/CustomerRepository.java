package com.h.udemy.java.uservices.customer.service.domain.ports.output.repository;

import com.h.udemy.java.uservices.customer.service.domain.entity.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository {

    Customer createCustomer(Customer customer);
    List<Customer> findAll();
    Customer findById(UUID id);
    Customer findByUsername(String username);
}
