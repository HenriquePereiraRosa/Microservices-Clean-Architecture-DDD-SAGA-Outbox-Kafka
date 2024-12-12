package com.h.udemy.java.uservices.order.service.domain.exception;

import com.h.udemy.java.uservices.domain.DomainException;
import com.h.udemy.java.uservices.domain.messages.Messages;

import java.util.UUID;

public class CustomerNotFoundException extends DomainException {
    public CustomerNotFoundException(UUID id) {
        super(Messages.ERR_CUSTOMER_NOT_FOUND.get() + id);
    }
}
