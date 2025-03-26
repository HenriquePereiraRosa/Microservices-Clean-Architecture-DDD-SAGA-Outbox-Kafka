package com.h.udemy.java.uservices.customer.service.domain.to.query;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class QueryCustomerResponse {
    private final UUID customerId;
    private final String username;
    private final String firstName;
    private final String lastName;
}
