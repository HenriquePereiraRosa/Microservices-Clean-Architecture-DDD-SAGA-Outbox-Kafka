package com.h.udemy.java.uservices.order.service.domain.exception;

import com.h.udemy.java.uservices.domain.messages.Messages;

public class OrderDomainTotalPriceException extends OrderDomainException {
    public OrderDomainTotalPriceException() {
        super(Messages.ERR_ORDER_PRICE_MUST_GREATER_ZERO.get());
    }
}
