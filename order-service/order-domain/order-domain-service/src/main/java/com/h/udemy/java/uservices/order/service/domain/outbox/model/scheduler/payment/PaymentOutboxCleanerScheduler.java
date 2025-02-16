package com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.payment;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_MESSAGES_DELETED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_MESSAGES_RECEIVED_FOR_CLEANUP;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.h.udemy.java.uservices.outbox.OutboxScheduler;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentOutboxCleanerScheduler implements OutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;

    public PaymentOutboxCleanerScheduler(PaymentOutboxHelper paymentOutboxHelper) {

        this.paymentOutboxHelper = paymentOutboxHelper;
    }

    @Override
    @Transactional
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {

        Optional<List<OrderPaymentOutboxMessage>> outboxMessageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.COMPLETED,
                        SagaStatus.SUCCEEDED,
                        SagaStatus.COMPENSATED,
                        SagaStatus.FAILED
                );

        if (outboxMessageResponse.isPresent()) {
            List<OrderPaymentOutboxMessage> outboxMessages = outboxMessageResponse.get();

            String concatenatedMessages = outboxMessages.stream()
                    .map(OrderPaymentOutboxMessage::getPayload)
                    .collect(Collectors.joining("\n"));

            log.info(ORDER_MESSAGES_RECEIVED_FOR_CLEANUP.build(
                    outboxMessages.size(),
                    OrderPaymentOutboxMessage.class.getSimpleName(),
                    concatenatedMessages));

            paymentOutboxHelper.delete(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.COMPENSATED,
                    SagaStatus.FAILED);

            log.info(ORDER_MESSAGES_DELETED.build(
                    outboxMessages.size(),
                    OrderPaymentOutboxMessage.class.getSimpleName(),
                    concatenatedMessages));
        }
    }

}
