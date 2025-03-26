package com.h.udemy.java.uservices.customer.service.domain;

import com.h.udemy.java.uservices.customer.service.domain.to.create.CreateCustomerCommand;
import com.h.udemy.java.uservices.customer.service.domain.entity.Customer;
import com.h.udemy.java.uservices.customer.service.domain.event.CustomerCreatedEvent;
import com.h.udemy.java.uservices.customer.service.domain.exception.CustomerDomainException;
import com.h.udemy.java.uservices.customer.service.domain.mapper.CustomerDataMapper;
import com.h.udemy.java.uservices.customer.service.domain.ports.output.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.CUSTOMER_ID_COULD_NOT_BE_SAVED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ID_CREATED_ORDER_RESPONSE;

@Slf4j
@Component
class CustomerCreateCommandHandler {

    private final CustomerDomainService customerDomainService;

    private final CustomerRepository customerRepository;

    private final CustomerDataMapper customerDataMapper;

    public CustomerCreateCommandHandler(CustomerDomainService customerDomainService,
                                        CustomerRepository customerRepository,
                                        CustomerDataMapper customerDataMapper) {
        this.customerDomainService = customerDomainService;
        this.customerRepository = customerRepository;
        this.customerDataMapper = customerDataMapper;
    }

    @Transactional
    public CustomerCreatedEvent createCustomer(CreateCustomerCommand createCustomerCommand) {
        Customer customer = customerDataMapper.createCustomerCommandToCustomer(createCustomerCommand);
        CustomerCreatedEvent customerCreatedEvent = customerDomainService.validateAndInitiateCustomer(customer);
        Customer savedCustomer = customerRepository.createCustomer(customer);

        if (savedCustomer == null) {
            final String msg = CUSTOMER_ID_COULD_NOT_BE_SAVED.build(createCustomerCommand.getCustomerId());
            log.error(msg);
            throw new CustomerDomainException(msg);
        }

        log.info(ID_CREATED_ORDER_RESPONSE.build(
                CustomerCreatedEvent.class.getSimpleName(),
                Customer.class.getSimpleName(),
                createCustomerCommand.getCustomerId()));

        return customerCreatedEvent;
    }

    @Transactional(readOnly = true)
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Customer findById(UUID id) {
        return customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }
}
