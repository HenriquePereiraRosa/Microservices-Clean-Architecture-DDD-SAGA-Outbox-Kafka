package com.h.udemy.java.uservices.order.service.domain.exception;

import static com.h.udemy.java.uservices.domain.messages.Msgs.ERR_ORDER_NOT_CORRECT_INIT_STATE;
import static com.h.udemy.java.uservices.domain.messages.Msgs.ERR_ORDER_PRICE_MUST_GREATER_ZERO;

public class OrderDomainInitialStateException extends OrderDomainException {
    public OrderDomainInitialStateException() {
        super(ERR_ORDER_NOT_CORRECT_INIT_STATE.get());
    }

    public OrderDomainInitialStateException(Throwable cause) {
        super(ERR_ORDER_PRICE_MUST_GREATER_ZERO.get(),
                cause);
    }
}
