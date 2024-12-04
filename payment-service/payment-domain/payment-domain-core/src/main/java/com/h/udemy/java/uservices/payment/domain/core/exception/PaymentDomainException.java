package com.h.udemy.java.uservices.payment.domain.core.exception;

import com.h.udemy.java.uservices.domain.DomainException;

public class PaymentDomainException extends DomainException {

    public PaymentDomainException(String message) {
        super(message);
    }
}
