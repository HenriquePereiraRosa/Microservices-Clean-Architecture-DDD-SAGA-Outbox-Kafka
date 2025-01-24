package com.h.udemy.java.uservices.saga.strategy;

import com.h.udemy.java.uservices.saga.SagaStatus;

public class CancellingSagaStatusStrategy implements SagaStatusStrategy {
    @Override
    public SagaStatus getSagaStatus() {
        return SagaStatus.COMPENSATING;
    }
}
