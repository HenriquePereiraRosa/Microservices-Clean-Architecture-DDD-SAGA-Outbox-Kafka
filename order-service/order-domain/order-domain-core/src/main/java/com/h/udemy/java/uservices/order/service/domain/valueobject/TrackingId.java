package com.h.udemy.java.uservices.order.service.domain.valueobject;

import com.h.udemy.java.uservices.domain.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID pValue) {
        super(pValue);
    }

    @Override
    public String toString() {
        return this.getValue().toString();
    }
}
