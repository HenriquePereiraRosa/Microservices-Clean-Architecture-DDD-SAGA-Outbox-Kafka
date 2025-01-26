package com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.payment;

import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.h.udemy.java.uservices.outbox.OutboxScheduler;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.*;

@Slf4j
@Component
public class PaymentOutboxCleanerScheduler implements OutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;
    private final PaymentRequestMessagePublisher paymentRequestMessagePublisher;

    public PaymentOutboxCleanerScheduler(PaymentOutboxHelper paymentOutboxHelper,
            PaymentRequestMessagePublisher paymentRequestMessagePublisher) {

        this.paymentOutboxHelper = paymentOutboxHelper;
        this.paymentRequestMessagePublisher = paymentRequestMessagePublisher;
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
