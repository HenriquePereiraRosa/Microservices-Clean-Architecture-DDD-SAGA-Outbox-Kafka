package com.h.udemy.java.uservices.customer.service.domain.ports.input.service;

import com.h.udemy.java.uservices.customer.service.domain.to.create.CreateCustomerCommand;
import com.h.udemy.java.uservices.customer.service.domain.to.create.CreateCustomerResponse;
import com.h.udemy.java.uservices.customer.service.domain.to.query.QueryCustomerResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface CustomerApplicationService {

    List<QueryCustomerResponse> findAll();
    QueryCustomerResponse findById(@Valid UUID id);
    QueryCustomerResponse findByUsername(@Valid String name);

    CreateCustomerResponse createCustomer(@Valid CreateCustomerCommand createCustomerCommand);

}
