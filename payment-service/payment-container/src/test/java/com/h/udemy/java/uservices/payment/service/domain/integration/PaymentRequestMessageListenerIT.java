package com.h.udemy.java.uservices.payment.service.domain.integration;

import com.h.udemy.java.uservices.domain.valueobject.PaymentOrderStatus;
import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.ports.input.message.listener.PaymentRequestMessageListener;
import com.h.udemy.java.uservices.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.h.udemy.java.uservices.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import com.h.udemy.java.uservices.payment.service.domain.PaymentServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.h.udemy.java.uservices.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(classes = PaymentServiceApplication.class)
@ActiveProfiles("integration-tests")
public class PaymentRequestMessageListenerIT {

    @Autowired
    private PaymentRequestMessageListener paymentRequestMessageListener;

    @Autowired
    private OrderOutboxJpaRepository orderOutboxJpaRepository;

    private final static String CUSTOMER_ID = "d215b5f8-0249-4dc5-89a3-51fd148cfb41";
    private final static BigDecimal PRICE = new BigDecimal("100");

    @Test
    void givenFirstPaymentCompleted_whenCompletingPaymentAgain_thenThrowsDataAccessException() {
        String sagaId = UUID.randomUUID().toString();
        paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
        try {
            paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
        } catch (DataAccessException e) {
            log.error("DataAccessException occurred with sql state: {}",
                    ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
        }

        assertThatOrderOutboxHasBeenSaved(sagaId);
    }

    @Test
    void givenSameSagaId_whenCompletePaymentCalledConcurrently_thenEnsureSinglePaymentProcessed() {
        String sagaId = UUID.randomUUID().toString();
        ExecutorService executor = null;

        try {
            executor = Executors.newFixedThreadPool(2);
            List<Callable<Object>> tasks = new ArrayList<>();

            tasks.add(Executors.callable(() -> {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
                } catch (DataAccessException e) {
                    log.error("DataAccessException occurred for thread 1 with sql state: {}",
                            ((PSQLException) requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            tasks.add(Executors.callable(() -> {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
                } catch (DataAccessException e) {
                    log.error("DataAccessException occurred for thread 2 with sql state: {}",
                            ((PSQLException) requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            executor.invokeAll(tasks);

            assertThatOrderOutboxHasBeenSaved(sagaId);
        } catch (InterruptedException e) {
            log.error("Error calling complete payment!", e);
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }

    private void assertThatOrderOutboxHasBeenSaved(final String expectedSagaId) {
        Optional<OrderOutboxEntity> orderOutboxEntity = orderOutboxJpaRepository
                .findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(
                        ORDER_SAGA_NAME,
                        UUID.fromString(expectedSagaId),
                        PaymentStatus.COMPLETED,
                        OutboxStatus.STARTED);
        assertTrue(orderOutboxEntity.isPresent());
        assertEquals(orderOutboxEntity.get().getSagaId().toString(), expectedSagaId);
    }

    private PaymentRequest getPaymentRequest(String sagaId) {
        return PaymentRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(sagaId)
                .orderId(UUID.randomUUID().toString())
                .paymentOrderStatus(PaymentOrderStatus.PENDING)
                .customerId(CUSTOMER_ID)
                .price(PRICE)
                .createdAt(Instant.now())
                .build();
    }

}