package com.h.udemy.java.uservices.application.rest;

import com.h.udemy.java.uservices.service.create.CreateCustomerCommand;
import com.h.udemy.java.uservices.service.create.CreateCustomerResponse;
import com.h.udemy.java.uservices.service.ports.input.service.CustomerApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.CUSTOMER_CONTROLLER_CREATE;

@Slf4j
@RestController
@RequestMapping(value = "/customers", produces = "application/vnd.api.v1+json")
public class CustomerController {

    private final CustomerApplicationService customerApplicationService;

    public CustomerController(CustomerApplicationService customerApplicationService) {
        this.customerApplicationService = customerApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerCommand
                                                                             createCustomerCommand) {

        log.info(CUSTOMER_CONTROLLER_CREATE.build(
                createCustomerCommand.getUsername()));

        CreateCustomerResponse response = customerApplicationService.createCustomer(createCustomerCommand);

        return ResponseEntity.ok(response);
    }
}
