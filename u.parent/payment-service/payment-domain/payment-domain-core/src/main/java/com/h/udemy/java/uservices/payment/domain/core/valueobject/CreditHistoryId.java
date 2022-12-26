package com.h.udemy.java.uservices.payment.domain.core.valueobject;

import com.h.udemy.java.uservices.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditHistoryId extends BaseId<UUID> {

    public CreditHistoryId(UUID pValue) {
        super(pValue);
    }
}
