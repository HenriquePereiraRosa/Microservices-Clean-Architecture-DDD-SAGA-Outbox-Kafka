package com.h.udemy.java.uservices.order.service.dataaccess.outbox.payment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.h.udemy.java.uservices.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;

@Repository
public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutboxEntity, UUID> {

    Optional<List<PaymentOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                                                 OutboxStatus outboxStatus,
                                                                                 List<SagaStatus> sagaStatus);

    Optional<PaymentOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type,
                                                                     UUID sagaId,
                                                                     List<SagaStatus> sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                    OutboxStatus outboxStatus,
                                                    List<SagaStatus> sagaStatus);

}
