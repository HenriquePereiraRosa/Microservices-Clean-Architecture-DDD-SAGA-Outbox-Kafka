package com.h.udemy.java.uservices.application.graphql;

import com.h.udemy.java.uservices.service.create.CreateCustomerCommand;
import com.h.udemy.java.uservices.service.create.CreateCustomerResponse;
import com.h.udemy.java.uservices.service.ports.input.service.CustomerApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.CUSTOMER_CONTROLLER_CREATE;

@Slf4j
@Controller
@Validated
public class CustomerController {

    private final CustomerApplicationService customerApplicationService;

    public CustomerController(CustomerApplicationService customerApplicationService) {
        this.customerApplicationService = customerApplicationService;
    }

    @MutationMapping
    public CreateCustomerResponse createCustomer(@Argument CreateCustomerCommand createCustomerCommand) {

        log.info(CUSTOMER_CONTROLLER_CREATE.build(createCustomerCommand.getUsername()));

        return customerApplicationService.createCustomer(createCustomerCommand);
    }
}
