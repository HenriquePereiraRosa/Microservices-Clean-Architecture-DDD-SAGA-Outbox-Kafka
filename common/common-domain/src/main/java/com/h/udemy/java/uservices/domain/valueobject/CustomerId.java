package com.h.udemy.java.uservices.domain.valueobject;

import java.util.UUID;

public class CustomerId extends BaseId<UUID> {
    public CustomerId(UUID id) {
        super(id);
    }
    public CustomerId(String id) {
        super(UUID.fromString(id));
    }
}
