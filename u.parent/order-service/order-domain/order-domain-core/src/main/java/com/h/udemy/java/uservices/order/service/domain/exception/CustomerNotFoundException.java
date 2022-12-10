package com.h.udemy.java.uservices.order.service.domain.exception;

import com.h.udemy.java.uservices.domain.DomainException;
import com.h.udemy.java.uservices.order.service.domain.messages.I18n;

import java.util.UUID;

public class CustomerNotFoundException extends DomainException {
    public CustomerNotFoundException(UUID id) {
        super(I18n.ERR_CUSTOMER_NOT_FOUND.getMsg() + id);
    }
}
