package com.h.udemy.java.uservices.customer.service.graphql;

import com.h.udemy.java.uservices.customer.service.domain.ports.input.service.CustomerApplicationService;
import com.h.udemy.java.uservices.customer.service.domain.to.query.QueryCustomerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@Validated
public class QueryCustomerController {

    private final CustomerApplicationService customerApplicationService;

    public QueryCustomerController(CustomerApplicationService customerApplicationService) {
        this.customerApplicationService = customerApplicationService;
    }

    @QueryMapping
    public Flux<List<QueryCustomerResponse>> findAllCustomers() {
        return Flux.just(customerApplicationService.findAll());
    }

    @QueryMapping
    public Mono<QueryCustomerResponse> findCustomerById(@Argument UUID id) {

        return Mono.fromSupplier(() -> customerApplicationService.findById(id));
    }

    @QueryMapping
    public Mono<QueryCustomerResponse> findCustomerByUsername(@Argument String username) {

        return Mono.fromSupplier(() -> customerApplicationService.findByUsername(username));
    }
}
