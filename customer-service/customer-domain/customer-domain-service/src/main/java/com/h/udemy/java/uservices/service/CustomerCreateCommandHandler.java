package com.h.udemy.java.uservices.service;

import com.h.udemy.java.uservices.service.create.CreateCustomerCommand;
import com.h.udemy.java.uservices.core.CustomerDomainService;
import com.h.udemy.java.uservices.core.entity.Customer;
import com.h.udemy.java.uservices.core.event.CustomerCreatedEvent;
import com.h.udemy.java.uservices.core.exception.CustomerDomainException;
import com.h.udemy.java.uservices.service.mapper.CustomerDataMapper;
import com.h.udemy.java.uservices.service.ports.output.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
}
