package com.h.udemy.java.uservices.payment.domain.core.valueobject;

import com.h.udemy.java.uservices.domain.valueobject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {

    public PaymentId(UUID pValue) {
        super(pValue);
    }
}
