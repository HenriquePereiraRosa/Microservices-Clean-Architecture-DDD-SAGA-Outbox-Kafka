package com.h.udemy.java.uservices.restaurant.domain.core.valueobject;

import com.h.udemy.java.uservices.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {
    public OrderApprovalId(UUID pValue) {
        super(pValue);
    }
}
