package com.h.udemy.java.uservices.order.service.domain.outbox.model.payment;

import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderPaymentOutboxMessage {

    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    @Setter private ZonedDateTime processedAt;
    private String type;
    private String payload;
    @Setter private SagaStatus sagaStatus;
    @Setter private OrderStatus orderStatus;
    @Setter private OutboxStatus outBoxStatus;
    private int version;
}
