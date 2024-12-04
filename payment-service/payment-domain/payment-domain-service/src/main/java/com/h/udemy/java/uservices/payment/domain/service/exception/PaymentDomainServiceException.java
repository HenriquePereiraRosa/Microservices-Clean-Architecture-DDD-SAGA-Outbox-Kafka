package com.h.udemy.java.uservices.payment.domain.service.exception;

import com.h.udemy.java.uservices.domain.DomainException;

public class PaymentDomainServiceException extends DomainException {
    public PaymentDomainServiceException(String message) {
        super(message);
    }
}