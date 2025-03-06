package com.h.udemy.java.uservices.payment.domain.service.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;

import java.math.BigDecimal;
import java.util.UUID;

import static com.h.udemy.java.uservices.constants.TestConstants.CUSTOMER_ID;


public class CreditEntryFactory {

    private static final double CREDIT_VALUE = 1002.22;

    static public CreditEntry createOne() {

        return CreditEntry.builder()
                .customerId(new CustomerId(CUSTOMER_ID))
                .totalCreditAmount(new Money(new BigDecimal(CREDIT_VALUE)))
                .build();
    }

    static public CreditEntry createOne(double value) {

        return CreditEntry.builder()
                .customerId(new CustomerId(CUSTOMER_ID))
                .totalCreditAmount(new Money(new BigDecimal(value)))
                .build();
    }

    static public CreditEntry createOne(UUID pCustomerId) {

        return CreditEntry.builder()
                .customerId(new CustomerId(pCustomerId))
                .totalCreditAmount(new Money(new BigDecimal(CREDIT_VALUE)))
                .build();
    }
}
