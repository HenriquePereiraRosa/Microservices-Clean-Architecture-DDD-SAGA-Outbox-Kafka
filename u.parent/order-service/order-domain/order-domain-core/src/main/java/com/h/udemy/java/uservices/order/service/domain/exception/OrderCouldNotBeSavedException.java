package com.h.udemy.java.uservices.order.service.domain.exception;

import com.h.udemy.java.uservices.domain.DomainException;
import com.h.udemy.java.uservices.domain.messages.Msgs;

import java.util.UUID;

public class OrderCouldNotBeSavedException extends DomainException {
    public OrderCouldNotBeSavedException(UUID orderId) {
        super(Msgs.ERR_ORDER_COULD_NOT_BE_SAVED.get() + orderId);
    }
}
