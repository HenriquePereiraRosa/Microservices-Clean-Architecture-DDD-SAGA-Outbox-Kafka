package com.h.udemy.java.uservices.order.service.domain.integration;

import com.h.udemy.java.uservices.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.h.udemy.java.uservices.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.h.udemy.java.uservices.order.service.domain.OrderServiceApi;
import com.h.udemy.java.uservices.order.service.domain.saga.OrderPaymentSaga;
import com.h.udemy.java.uservices.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static com.h.udemy.java.uservices.order.service.domain.test.utils.factory.PaymentResponseMockFactory.mockPaymentResponse;
import static com.h.udemy.java.uservices.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = OrderServiceApi.class)
@Sql(value = {"classpath:sql/OrderPaymentSagaTestSetup.sql"})
@Sql(value = {"classpath:sql/OrderPaymentSagaTestCleanup.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Slf4j
@ActiveProfiles("integration-tests")
class OrderPaymentSagaIT {

    public static final UUID SAGA_ID = UUID.fromString("15a497c1-0f4b-4eff-b9f4-c402c8c07afa");

    @Autowired
    private OrderPaymentSaga orderPaymentSaga;

    @Autowired
    private PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    @Test
    void should_process_Multiple_Payments_at_same_time() {
        orderPaymentSaga.process(mockPaymentResponse(SAGA_ID));
        orderPaymentSaga.process(mockPaymentResponse(SAGA_ID));
    }

    @Test
    void should_process_Multiple_Payments_at_same_time_AND_in_different_THREADS() throws InterruptedException {
        AtomicReference<Throwable> exceptionHolder = new AtomicReference<>();

        Thread thread1 = new Thread(() -> {
            try {
                orderPaymentSaga.process(mockPaymentResponse(SAGA_ID));
            } catch (ObjectOptimisticLockingFailureException e) {
                exceptionHolder.set(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                orderPaymentSaga.process(mockPaymentResponse(SAGA_ID));
            } catch (ObjectOptimisticLockingFailureException e) {
                exceptionHolder.set(e);
            }
        });

        thread1.start();
        thread2.start();

        // join() forces the both Threads
        // to be executed before the
        // main Thread leaves the test!
        thread1.join();
        thread2.join();

        assertThatOnePaymentOutboxIsSavedInDb();
        assertThat(exceptionHolder.get()).isNotNull();
    }

    @Test
    void should_process_Multiple_Payments_at_same_time_AND_in_different_THREADS_With_LATCH() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        AtomicReference<Throwable> exceptionHolder = new AtomicReference<>();

        Thread thread1 = new Thread(() -> {
            try {
                orderPaymentSaga.process(mockPaymentResponse(SAGA_ID));
            } catch (OptimisticLockingFailureException e) {
                exceptionHolder.set(e);
                log.error("OptimisticLockingFailureException occurred for thread1");
            } finally {
                latch.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                orderPaymentSaga.process(mockPaymentResponse(SAGA_ID));
            } catch (OptimisticLockingFailureException e) {
                exceptionHolder.set(e);
                log.error("OptimisticLockingFailureException occurred for thread2");
            } finally {
                latch.countDown();
            }
        });

        thread1.start();
        thread2.start();

        latch.await();

        assertThatOnePaymentOutboxIsSavedInDb();
        assertThat(exceptionHolder.get()).isNotNull();
    }

    private void assertThatOnePaymentOutboxIsSavedInDb() {
        Optional<PaymentOutboxEntity> paymentOutboxEntityOp =
                paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(
                        ORDER_SAGA_NAME,
                        SAGA_ID,
                        List.of(SagaStatus.PROCESSING));

        assertThat(paymentOutboxEntityOp).isPresent();
    }


}