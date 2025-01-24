package com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.h.udemy.java.uservices.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface RestaurantApprovalRequestMessagePublisher
        extends DomainEventPublisher<OrderPaidEvent> {

    void publish(
            OrderApprovalOutboxMessage orderApprovalOutboxMessage,
            BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);
}
