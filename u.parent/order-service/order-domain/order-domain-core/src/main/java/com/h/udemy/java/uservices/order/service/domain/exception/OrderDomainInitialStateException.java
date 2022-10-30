package com.h.udemy.java.uservices.order.service.domain.exception;

import com.h.udemy.java.uservices.order.service.domain.messages.I18n;

public class OrderDomainInitialStateException extends OrderDomainException {
    public OrderDomainInitialStateException() {
        super(I18n.ERR_ORDER_NOT_CORRECT_INIT_STATE.getMsg());
    }

    public OrderDomainInitialStateException(Throwable cause) {
        super(I18n.ERR_ORDER_PRICE_MUST_GREATER_ZERO.getMsg(),
                cause);
    }
}
