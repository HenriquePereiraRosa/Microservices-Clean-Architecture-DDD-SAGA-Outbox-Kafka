package com.h.udemy.java.uservices.domain.valueobject;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {
    protected OrderId(UUID pValue) {
        super(pValue);
    }
}
