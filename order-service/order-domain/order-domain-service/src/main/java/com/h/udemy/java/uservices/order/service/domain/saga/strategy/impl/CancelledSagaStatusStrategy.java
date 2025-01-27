package com.h.udemy.java.uservices.order.service.domain.saga.strategy.impl;

import com.h.udemy.java.uservices.order.service.domain.saga.strategy.OrderPaymentSagaStatusStrategy;
import com.h.udemy.java.uservices.saga.SagaStatus;

public class CancelledSagaStatusStrategy implements OrderPaymentSagaStatusStrategy {
    @Override
    public SagaStatus[] getSagaStatus() {
        return new SagaStatus[]{ SagaStatus.PROCESSING };
    }
}
