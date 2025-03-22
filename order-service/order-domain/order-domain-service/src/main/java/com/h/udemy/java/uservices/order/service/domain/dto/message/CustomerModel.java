package com.h.udemy.java.uservices.order.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomerModel {

    private final String id;
    private final String username;
    private final String firstName;
    private final String lastName;
}
