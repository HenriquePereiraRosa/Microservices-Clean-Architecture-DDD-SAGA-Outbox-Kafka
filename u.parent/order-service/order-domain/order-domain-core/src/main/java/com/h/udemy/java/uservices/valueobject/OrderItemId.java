package com.h.udemy.java.uservices.valueobject;

import com.h.udemy.java.uservices.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long id) {
        super(id);
    }
}
