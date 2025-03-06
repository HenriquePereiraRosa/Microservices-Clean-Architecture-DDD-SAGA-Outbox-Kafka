package com.h.udemy.java.uservices.outbox;

import org.springframework.stereotype.Component;

@Component
class OutboxSchedulerMock implements OutboxScheduler {

    @Override
    public void processOutboxMessage() {
        // mock class do nothing
    }
}