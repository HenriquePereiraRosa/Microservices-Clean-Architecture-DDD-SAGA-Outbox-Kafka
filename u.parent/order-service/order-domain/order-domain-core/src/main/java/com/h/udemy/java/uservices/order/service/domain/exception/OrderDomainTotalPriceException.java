package com.h.udemy.java.uservices.order.service.domain.exception;

import com.h.udemy.java.uservices.order.service.domain.exception.msg.I18n;

public class OrderDomainTotalPriceException extends OrderDomainException {
    public OrderDomainTotalPriceException() {
        super(I18n.ERR_ORDER_PRICE_MUST_GREATER_ZERO.getMsg());
    }

    public OrderDomainTotalPriceException(Throwable cause) {
        super(I18n.ERR_ORDER_PRICE_MUST_GREATER_ZERO.getMsg(),
                cause);
    }
}
