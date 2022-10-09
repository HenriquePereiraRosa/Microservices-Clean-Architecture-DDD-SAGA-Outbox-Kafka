package com.h.udemy.java.uservices.domain.valueobject;

import java.util.UUID;

public class ProductId extends BaseId<UUID> {
    protected ProductId(UUID pValue) {
        super(pValue);
    }
}
