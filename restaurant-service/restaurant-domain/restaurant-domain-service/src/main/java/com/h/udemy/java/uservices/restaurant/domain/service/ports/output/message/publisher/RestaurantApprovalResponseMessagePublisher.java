package com.h.udemy.java.uservices.restaurant.domain.service.ports.output.message.publisher;

import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.restaurant.domain.service.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface RestaurantApprovalResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
