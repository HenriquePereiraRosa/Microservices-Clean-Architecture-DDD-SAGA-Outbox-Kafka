package com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.approval;

import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.OutboxProcessor;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.h.udemy.java.uservices.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.ERR_OUTBOX_MESSAGE_COULD_NOT_BE_SAVED;
import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.OUTBOX_MESSAGE_SAVED;
import static com.h.udemy.java.uservices.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static java.util.Objects.isNull;

@Slf4j
@Component
public class ApprovalOutboxHelper {

    private final ApprovalOutboxRepository approvalOutboxRepository;

    public ApprovalOutboxHelper(ApprovalOutboxRepository approvalOutboxRepository) {
        this.approvalOutboxRepository = approvalOutboxRepository;
    }

    @Transactional(readOnly = true)
    public Optional<List<OrderApprovalOutboxMessage>>
    getApprovalOutboxMessageByOutboxStatusAndSagaStatus(OutboxProcessor outboxProcessor) {

        return approvalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(
                ORDER_SAGA_NAME,
                outboxProcessor.outboxStatus(),
                outboxProcessor.sagaStatuses());

    }

    @Transactional(readOnly = true)
    public Optional<OrderApprovalOutboxMessage>
    getPaymentOutboxMessageBySagaIdAndSagaStatus(
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
    public void deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(OutboxProcessor outboxProcessor) {

        approvalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(
                ORDER_SAGA_NAME,
                outboxProcessor.outboxStatus(),
                outboxProcessor.sagaStatuses());
    }
}
