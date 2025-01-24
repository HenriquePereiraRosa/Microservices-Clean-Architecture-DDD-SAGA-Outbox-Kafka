package com.h.udemy.java.uservices.saga.strategy;

import com.h.udemy.java.uservices.saga.SagaStatus;

public class CancelledSagaStatusStrategy implements SagaStatusStrategy {
    @Override
    public SagaStatus getSagaStatus() {
        return SagaStatus.COMPENSATED;
    }
}
