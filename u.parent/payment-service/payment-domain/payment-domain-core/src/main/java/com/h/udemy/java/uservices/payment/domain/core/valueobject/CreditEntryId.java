package com.h.udemy.java.uservices.payment.domain.core.valueobject;

import com.h.udemy.java.uservices.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {

    public CreditEntryId(UUID pValue) {
        super(pValue);
    }
}
