package com.h.udemy.java.uservices.core.exception;

import com.h.udemy.java.uservices.domain.DomainException;

public class CustomerDomainException extends DomainException {

    public CustomerDomainException(String message) {
        super(message);
    }
}
