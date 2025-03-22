package com.h.udemy.java.uservices.service;

import com.h.udemy.java.uservices.service.create.CreateCustomerCommand;
import com.h.udemy.java.uservices.service.create.CreateCustomerResponse;
import com.h.udemy.java.uservices.core.entity.Customer;
import com.h.udemy.java.uservices.core.event.CustomerCreatedEvent;
import com.h.udemy.java.uservices.service.mapper.CustomerDataMapper;
import com.h.udemy.java.uservices.service.ports.input.service.CustomerApplicationService;
import com.h.udemy.java.uservices.service.ports.output.message.publisher.CustomerMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.SAVED_SUCCESSFULLY;

@Slf4j
@Validated
@Service
class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final CustomerCreateCommandHandler customerCreateCommandHandler;

    private final CustomerDataMapper customerDataMapper;

    private final CustomerMessagePublisher customerMessagePublisher;

    public CustomerApplicationServiceImpl(CustomerCreateCommandHandler customerCreateCommandHandler,
                                          CustomerDataMapper customerDataMapper,
                                          CustomerMessagePublisher customerMessagePublisher) {
        this.customerCreateCommandHandler = customerCreateCommandHandler;
        this.customerDataMapper = customerDataMapper;
        this.customerMessagePublisher = customerMessagePublisher;
    }

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {
        CustomerCreatedEvent customerCreatedEvent = customerCreateCommandHandler.createCustomer(createCustomerCommand);
        customerMessagePublisher.publish(customerCreatedEvent);
        return customerDataMapper
                .customerToCreateCustomerResponse(customerCreatedEvent.getCustomer(),
                        SAVED_SUCCESSFULLY.build(Customer.class.getSimpleName()));
    }
}
