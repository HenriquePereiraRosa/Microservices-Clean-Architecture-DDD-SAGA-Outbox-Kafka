package com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.approval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.Constants.getZonedDateTimeNow;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;
import static com.h.udemy.java.uservices.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static java.util.Objects.isNull;

@Slf4j
@Component
public class ApprovalOutboxHelper {

    private final ApprovalOutboxRepository approvalOutboxRepository;
    private final ObjectMapper objectMapper;

    public ApprovalOutboxHelper(
            ApprovalOutboxRepository approvalOutboxRepository,
            ObjectMapper objectMapper) {

        this.approvalOutboxRepository = approvalOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderApprovalOutboxMessage>>
    getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses) {

        return approvalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(
                ORDER_SAGA_NAME,
                outboxStatus,
                sagaStatuses);

    }

    @Transactional(readOnly = true)
    public Optional<OrderApprovalOutboxMessage>
    getApprovalOutboxMessageBySagaIdAndSagaStatus(
            UUID uuid,
            SagaStatus... sagaStatuses) {

        return approvalOutboxRepository.findByTypeAndSagaIdAndSagaStatus(
                ORDER_SAGA_NAME,
                uuid,
                sagaStatuses);

    }

    @Transactional
    public void save(OrderApprovalOutboxMessage outboxMessage) {

        OrderApprovalOutboxMessage response = approvalOutboxRepository.save(outboxMessage);

        if (isNull(response)) {
            final String errorMsg = ERR_OUTBOX_MESSAGE_COULD_NOT_BE_SAVED.build(
                    OrderApprovalOutboxMessage.class.getSimpleName(),
                    outboxMessage.getId());

            log.error(errorMsg);
            throw new OrderDomainException(errorMsg);
        }

        log.info(OUTBOX_MESSAGE_SAVED.build(
                OrderApprovalOutboxMessage.class.getSimpleName(),
                outboxMessage.getId()));

    }

    @Transactional
    public void saveApprovalOutboxMessage(
            OrderApprovalEventPayload orderApprovalEventPayload,
            OrderStatus orderStatus,
            SagaStatus sagaStatus,
            OutboxStatus outboxStatus,
            UUID sagaId) {

        save(OrderApprovalOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(getZonedDateTimeNow())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderApprovalEventPayload))
                .orderStatus(orderStatus)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    @Transactional
    public void deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses) {

        approvalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(
                ORDER_SAGA_NAME,
                outboxStatus,
                sagaStatuses);
    }

    private String createPayload(OrderApprovalEventPayload approvalEventPayload) {
        try {
            return objectMapper.writeValueAsString(approvalEventPayload);
        } catch (JsonProcessingException e) {

            final String eMsg = ERR_ORDER_COULD_NOT_BE_MAPPED.build(
                    OrderApprovalEventPayload.class.getSimpleName(),
                    approvalEventPayload.getOrderId());
            log.info(eMsg);
            throw new OrderDomainException(eMsg, e);
        }
    }
}
