package com.h.udemy.java.uservices.order.service.dataaccess.outbox.restaurantapproval.adapter;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_OBJ_COULD_NOT_BE_FOUND;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.h.udemy.java.uservices.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.h.udemy.java.uservices.order.service.dataaccess.outbox.restaurantapproval.exception.ApprovalOutboxNotFoundException;
import com.h.udemy.java.uservices.order.service.dataaccess.outbox.restaurantapproval.mapper.ApprovalOutboxDataAccessMapper;
import com.h.udemy.java.uservices.order.service.dataaccess.outbox.restaurantapproval.repository.ApprovalOutboxJpaRepository;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;

@Component
public class ApprovalOutboxRepositoryImpl implements ApprovalOutboxRepository {

    private final ApprovalOutboxJpaRepository approvalOutboxJpaRepository;
    private final ApprovalOutboxDataAccessMapper approvalOutboxDataAccessMapper;

    public ApprovalOutboxRepositoryImpl(ApprovalOutboxJpaRepository approvalOutboxJpaRepository,
            ApprovalOutboxDataAccessMapper approvalOutboxDataAccessMapper) {
        this.approvalOutboxJpaRepository = approvalOutboxJpaRepository;
        this.approvalOutboxDataAccessMapper = approvalOutboxDataAccessMapper;
    }

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return approvalOutboxDataAccessMapper
                .approvalOutboxEntityToOrderApprovalOutboxMessage(approvalOutboxJpaRepository
                        .save(approvalOutboxDataAccessMapper
                                .orderCreatedOutboxMessageToOutboxEntity(orderApprovalOutboxMessage)));
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>>
    findByTypeAndOutboxStatusAndSagaStatus(
            String sagaType,
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatus) {

        return Optional.of(approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(sagaType, outboxStatus,
                        Arrays.asList(sagaStatus))
                .orElseThrow(() -> new ApprovalOutboxNotFoundException(OUTBOX_OBJ_COULD_NOT_BE_FOUND.build(
                        ApprovalOutboxEntity.class.getSimpleName(),
                        sagaType)))
                .stream()
                .map(approvalOutboxDataAccessMapper::approvalOutboxEntityToOrderApprovalOutboxMessage)
                .toList());
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(
            String type,
            UUID sagaId,
            SagaStatus... sagaStatus) {

        return approvalOutboxJpaRepository
                .findByTypeAndSagaIdAndSagaStatusIn(type, sagaId,
                        Arrays.asList(sagaStatus))
                .map(approvalOutboxDataAccessMapper::approvalOutboxEntityToOrderApprovalOutboxMessage);

    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(
            String type,
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatus) {

        approvalOutboxJpaRepository
                .deleteByTypeAndOutboxStatusAndSagaStatusIn(
                        type,
                        outboxStatus,
                        Arrays.asList(sagaStatus));
    }
}
