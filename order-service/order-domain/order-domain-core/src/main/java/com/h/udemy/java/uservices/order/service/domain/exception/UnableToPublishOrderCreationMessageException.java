package com.h.udemy.java.uservices.order.service.domain.exception;

import com.h.udemy.java.uservices.domain.DomainException;
import com.h.udemy.java.uservices.domain.messages.Messages;


public class UnableToPublishOrderCreationMessageException extends DomainException {
    public UnableToPublishOrderCreationMessageException(String id) {
        super(Messages.ERR_ORDER_COULD_NOT_BE_PUBLISHED.get() + id);
    }
}
