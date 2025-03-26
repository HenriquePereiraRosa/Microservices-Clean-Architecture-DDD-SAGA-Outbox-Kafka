package com.h.udemy.java.uservices.customer.service.domain.mapper;

import com.h.udemy.java.uservices.customer.service.domain.to.create.CreateCustomerCommand;
import com.h.udemy.java.uservices.customer.service.domain.to.create.CreateCustomerResponse;
import com.h.udemy.java.uservices.customer.service.domain.to.query.QueryCustomerResponse;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.customer.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataMapper {

    public Customer createCustomerCommandToCustomer(CreateCustomerCommand createCustomerCommand) {
        return new Customer(
                new CustomerId(createCustomerCommand.getCustomerId()),
                createCustomerCommand.getUsername(),
                createCustomerCommand.getFirstName(),
                createCustomerCommand.getLastName());
    }

    public CreateCustomerResponse customerToCreateCustomerResponse(
            Customer customer, String message) {

        return new CreateCustomerResponse(customer.getId().getValue(), message);
    }

    public QueryCustomerResponse customerToQueryCustomerResponse(Customer customer) {
        return QueryCustomerResponse.builder()
                .customerId(customer.getId().getValue())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}
