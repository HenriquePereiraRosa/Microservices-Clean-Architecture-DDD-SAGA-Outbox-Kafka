package com.h.udemy.java.uservices.customer.service.graphql;

import com.h.udemy.java.uservices.customer.service.domain.ports.input.service.CustomerApplicationService;
import com.h.udemy.java.uservices.customer.service.domain.to.create.CreateCustomerCommand;
import com.h.udemy.java.uservices.customer.service.domain.to.create.CreateCustomerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.CUSTOMER_CONTROLLER_CREATE;

@Slf4j
@Controller
@Validated
public class MutationCustomerController {

    private final CustomerApplicationService customerApplicationService;

    public MutationCustomerController(CustomerApplicationService customerApplicationService) {
        this.customerApplicationService = customerApplicationService;
    }

    @MutationMapping
    public Mono<CreateCustomerResponse> createCustomer(@Argument CreateCustomerCommand createCustomerCommand) {

        log.info(CUSTOMER_CONTROLLER_CREATE.build(createCustomerCommand.getUsername()));

        return Mono.fromSupplier(() -> customerApplicationService.createCustomer(createCustomerCommand));
    }
}
