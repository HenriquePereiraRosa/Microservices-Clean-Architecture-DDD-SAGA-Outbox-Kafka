package com.h.udemy.java.uservices.restaurant.domain.service.outbox.model;

import com.h.udemy.java.uservices.domain.valueobject.OrderApprovalStatus;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderOutboxMessage {
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    @Setter
    private OutboxStatus outboxStatus;
    private OrderApprovalStatus approvalStatus;
    private int version;
}
