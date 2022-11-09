package com.h.udemy.java.uservices.order.service.domain.ports.output.repository;

import com.h.udemy.java.uservices.order.service.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface ICustomerRepository {

    Customer insertCustomer(Customer customer);

    Optional<Customer> findCustomer(UUID customerId);
}