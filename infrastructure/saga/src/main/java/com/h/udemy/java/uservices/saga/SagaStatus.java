package com.h.udemy.java.uservices.saga;

public enum SagaStatus {

    STARTED,
    FAILED,
    SUCCEEDED,
    PROCESSING,
    COMPENSATING,
    COMPENSATED

}