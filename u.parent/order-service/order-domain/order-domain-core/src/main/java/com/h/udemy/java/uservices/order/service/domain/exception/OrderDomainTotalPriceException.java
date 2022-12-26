package com.h.udemy.java.uservices.order.service.domain.exception;

import com.h.udemy.java.uservices.domain.messages.Msgs;

public class OrderDomainTotalPriceException extends OrderDomainException {
    public OrderDomainTotalPriceException() {
        super(Msgs.ERR_ORDER_PRICE_MUST_GREATER_ZERO.get());
    }

    public OrderDomainTotalPriceException(Throwable cause) {
        super(Msgs.ERR_ORDER_PRICE_MUST_GREATER_ZERO.get(),
                cause);
    }
}
