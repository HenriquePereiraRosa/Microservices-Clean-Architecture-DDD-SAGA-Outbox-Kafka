package com.h.udemy.java.uservices.order.service.domain.exception;

import com.h.udemy.java.uservices.domain.DomainException;
import com.h.udemy.java.uservices.domain.messages.Msgs;

import java.util.UUID;

public class CustomerNotFoundException extends DomainException {
    public CustomerNotFoundException(UUID id) {
        super(Msgs.ERR_CUSTOMER_NOT_FOUND.get() + id);
    }
}
