package com.h.udemy.java.uservices.service.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateCustomerResponse {
    @NotNull
    private final UUID customerId;
    @NotNull
    private final String message;
}
