package com.h.udemy.java.uservices.order.service.dataaccess.customer.adapter;

import com.h.udemy.java.uservices.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.h.udemy.java.uservices.order.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.h.udemy.java.uservices.order.service.domain.entity.Customer;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryI implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerRepositoryI(CustomerJpaRepository customerJpaRepository,
                               CustomerDataAccessMapper customerDataAccessMapper) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepository.findById(customerId).map(customerDataAccessMapper::customerEntityToCustomer);
    }

    @Override
    public Customer save(Customer customer) {
        return customerDataAccessMapper.customerEntityToCustomer(
                customerJpaRepository.save(customerDataAccessMapper.customerToCustomerEntity(customer)));
    }
}
