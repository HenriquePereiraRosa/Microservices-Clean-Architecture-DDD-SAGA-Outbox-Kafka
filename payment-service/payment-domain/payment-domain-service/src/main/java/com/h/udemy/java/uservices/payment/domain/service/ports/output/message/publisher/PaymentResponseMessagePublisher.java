package com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher;

import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.payment.domain.service.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentResponseMessagePublisher {
    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
