package com.h.udemy.java.uservices.order.service.dataaccess.outbox.restaurantapproval.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.h.udemy.java.uservices.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;

@Repository
public interface ApprovalOutboxJpaRepository extends JpaRepository<ApprovalOutboxEntity, UUID> {

    Optional<List<ApprovalOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                                                  OutboxStatus outboxStatus,
                                                                                  List<SagaStatus> sagaStatus);

    Optional<ApprovalOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type,
                                                                      UUID sagaId,
                                                                      List<SagaStatus> sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                    OutboxStatus outboxStatus,
                                                    List<SagaStatus> sagaStatus);

}
