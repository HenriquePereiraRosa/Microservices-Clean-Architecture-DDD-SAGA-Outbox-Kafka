package com.h.udemy.java.uservices.customer.service.dataaccess.adapter;

import com.h.udemy.java.uservices.customer.service.dataaccess.repository.CustomerJpaRepository;
import com.h.udemy.java.uservices.customer.service.domain.entity.Customer;
import com.h.udemy.java.uservices.customer.service.dataaccess.mapper.CustomerDataAccessMapper;
import com.h.udemy.java.uservices.customer.service.domain.exception.CustomerNotFoundException;
import com.h.udemy.java.uservices.customer.service.domain.ports.output.repository.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository,
                                  CustomerDataAccessMapper customerDataAccessMapper) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerDataAccessMapper.customerEntityToCustomer(
                customerJpaRepository.save(customerDataAccessMapper.customerToCustomerEntity(customer)));
    }

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll()
                .stream().map(customerDataAccessMapper::customerEntityToCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public Customer findById(UUID id) {
        return customerDataAccessMapper.customerEntityToCustomer(
                customerJpaRepository.findById(id)
                        .orElseThrow(() -> new CustomerNotFoundException(String.valueOf(id))));
    }

    @Override
    public Customer findByUsername(String username) {
        return customerDataAccessMapper.customerEntityToCustomer(
                customerJpaRepository.findByUsername(username)
                        .orElseThrow(() -> new CustomerNotFoundException(username)));
    }
}
