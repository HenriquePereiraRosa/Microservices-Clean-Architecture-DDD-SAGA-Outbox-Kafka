package com.h.udemy.java.uservices.order.service.domain.exception;

import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_ORDER_NOT_CORRECT_INIT_STATE;

public class OrderDomainInitialStateException extends OrderDomainException {
    public OrderDomainInitialStateException() {
        super(ERR_ORDER_NOT_CORRECT_INIT_STATE.get());
    }
}
