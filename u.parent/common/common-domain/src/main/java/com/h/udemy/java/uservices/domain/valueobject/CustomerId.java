package com.h.udemy.java.uservices.domain.valueobject;

import java.util.UUID;

public class CustomerId extends BaseId<UUID> {
    public CustomerId(UUID pValue) {
        super(pValue);
    }
}
