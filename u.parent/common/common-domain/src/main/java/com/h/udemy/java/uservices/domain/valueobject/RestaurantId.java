package com.h.udemy.java.uservices.domain.valueobject;

import java.util.UUID;

public class RestaurantId extends BaseId<UUID> {
    protected RestaurantId(UUID pValue) {
        super(pValue);
    }
}
