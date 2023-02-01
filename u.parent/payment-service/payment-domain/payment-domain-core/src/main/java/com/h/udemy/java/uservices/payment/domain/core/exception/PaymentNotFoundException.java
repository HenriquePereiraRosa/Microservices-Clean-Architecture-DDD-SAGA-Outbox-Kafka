package com.h.udemy.java.uservices.payment.domain.core.exception;

import com.h.udemy.java.uservices.domain.DomainException;

public class PaymentNotFoundException extends DomainException {

    public PaymentNotFoundException(String message) {
        super(message);
    }
}
