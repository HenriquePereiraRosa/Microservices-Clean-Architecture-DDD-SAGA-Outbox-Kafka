package com.h.udemy.java.uservices.order.service.domain.test.config.bean;

import com.h.udemy.java.uservices.outbox.OutboxScheduler;
import org.springframework.stereotype.Component;

@Component
class OutboxSchedulerMock implements OutboxScheduler {

    @Override
    public void processOutboxMessage() {
        // Mock behavior, do nothing
    }
}