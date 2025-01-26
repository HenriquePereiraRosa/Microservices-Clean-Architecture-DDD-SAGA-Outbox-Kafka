package com.h.udemy.java.uservices.order.service.domain.outbox.model;

import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;


public record OutboxProcessor(
        OutboxStatus outboxStatus,
        SagaStatus... sagaStatuses) {

    public OutboxProcessor {
        // Validate or enforce immutability of the array
        if (sagaStatuses == null) {
            sagaStatuses = new SagaStatus[0];
        } else {
            sagaStatuses = sagaStatuses.clone(); // Defensive copy to ensure immutability
        }
    }
}
