package com.h.udemy.java.uservices.saga.strategy;

import com.h.udemy.java.uservices.saga.SagaStatus;

public interface SagaStatusStrategy {
    SagaStatus getSagaStatus();
}