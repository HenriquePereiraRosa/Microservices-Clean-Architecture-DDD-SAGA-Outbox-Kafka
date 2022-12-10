package com.h.udemy.java.uservices.order.service.domain.exception;

import com.h.udemy.java.uservices.domain.DomainException;
import com.h.udemy.java.uservices.order.service.domain.messages.I18n;

import java.util.UUID;

public class OrderCouldNotBeSavedException extends DomainException {
    public OrderCouldNotBeSavedException(UUID orderId) {
        super(I18n.ERR_ORDER_COULD_NOT_BE_SAVED.getMsg() + orderId);
    }
}
