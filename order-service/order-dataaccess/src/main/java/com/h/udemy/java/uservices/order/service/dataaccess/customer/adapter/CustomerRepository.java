package com.h.udemy.java.uservices.order.service.dataaccess.customer.adapter;

import com.h.udemy.java.uservices.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.h.udemy.java.uservices.order.service.dataaccess.customer.repository.ICustomerJpaRepository;
import com.h.udemy.java.uservices.order.service.domain.entity.Customer;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ICustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepository implements ICustomerRepository {

    private final ICustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerRepository(ICustomerJpaRepository customerJpaRepository,
                                  CustomerDataAccessMapper customerDataAccessMapper) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepository.findById(customerId).map(customerDataAccessMapper::customerEntityToCustomer);
    }
}
