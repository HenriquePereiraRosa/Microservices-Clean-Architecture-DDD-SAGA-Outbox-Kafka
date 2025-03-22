package com.h.udemy.java.uservices.service.mapper;

import com.h.udemy.java.uservices.service.create.CreateCustomerCommand;
import com.h.udemy.java.uservices.service.create.CreateCustomerResponse;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.core.entity.Customer;
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
}
