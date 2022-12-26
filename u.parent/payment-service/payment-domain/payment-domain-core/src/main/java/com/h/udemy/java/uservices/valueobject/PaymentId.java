package com.h.udemy.java.uservices.valueobject;

import com.h.udemy.java.uservices.domain.valueobject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {

    public PaymentId(UUID pValue) {
        super(pValue);
    }
}
