package com.h.udemy.java.uservices.order.service.domain.saga.strategy;

import com.h.udemy.java.uservices.saga.SagaStatus;

public interface OrderPaymentSagaStatusStrategy {
    SagaStatus[] getSagaStatus();
}