package com.h.udemy.java.uservices.customer.service.domain.to.create;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
public class CreateCustomerCommand {
    @NotNull(message = "customerId is required")
    @Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-4[a-fA-F0-9]{3}-[89abAB][a-fA-F0-9]{3}-[a-fA-F0-9]{12}$",
            message = "Invalid UUIDv4 for Username")
    private final String customerId; // todo: to remove (didactic purposes)

    @NotNull(message = "username is required")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]{3,20}$",
            message = "Username must be 3-20 characters (letters, digits, _, -, .)")
    private final String username;

    @NotNull(message = "firstName is required")
    @Pattern(regexp = "^[a-zA-Z]{1,30}$", message = "First name should contain letters only (max 30 characters)")
    private final String firstName;

    @NotNull(message = "lastName is required")
    @Pattern(regexp = "^[a-zA-Z]{1,30}$", message = "Last name should contain letters only (max 30 characters)")
    private final String lastName;
}
