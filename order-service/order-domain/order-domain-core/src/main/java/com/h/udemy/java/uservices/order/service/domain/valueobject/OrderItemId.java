package com.h.udemy.java.uservices.order.service.domain.valueobject;

import com.h.udemy.java.uservices.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long id) {
        super(id);
    }
}
