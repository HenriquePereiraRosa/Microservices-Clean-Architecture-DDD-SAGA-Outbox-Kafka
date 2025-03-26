package com.h.udemy.java.uservices.customer.service.domain;

import com.h.udemy.java.uservices.customer.service.domain.to.create.CreateCustomerCommand;
import com.h.udemy.java.uservices.customer.service.domain.to.create.CreateCustomerResponse;
import com.h.udemy.java.uservices.customer.service.domain.ports.input.service.CustomerApplicationService;
import com.h.udemy.java.uservices.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import com.h.udemy.java.uservices.customer.service.domain.entity.Customer;
import com.h.udemy.java.uservices.customer.service.domain.event.CustomerCreatedEvent;
import com.h.udemy.java.uservices.customer.service.domain.mapper.CustomerDataMapper;
import com.h.udemy.java.uservices.customer.service.domain.to.query.QueryCustomerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

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

    @Override
    public List<QueryCustomerResponse> findAll() {
        return customerCreateCommandHandler.findAllCustomers().stream()
                .map(customerDataMapper::customerToQueryCustomerResponse)
                .toList();
    }

    @Override
    public QueryCustomerResponse findById(UUID id) {
        return customerDataMapper.customerToQueryCustomerResponse(
                customerCreateCommandHandler.findById(id));
    }

    @Override
    public QueryCustomerResponse findByUsername(String name) {
        return customerDataMapper.customerToQueryCustomerResponse(
                customerCreateCommandHandler.findByUsername(name));
    }
}
