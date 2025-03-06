package com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.payment;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_STATUS_UPDATED;
import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.ORDER_ID_CREATING;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.h.udemy.java.uservices.outbox.OutboxScheduler;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentOutboxScheduler implements OutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;
    private final PaymentRequestMessagePublisher paymentRequestMessagePublisher;

    public PaymentOutboxScheduler(PaymentOutboxHelper paymentOutboxHelper,
            PaymentRequestMessagePublisher paymentRequestMessagePublisher) {

        this.paymentOutboxHelper = paymentOutboxHelper;
        this.paymentRequestMessagePublisher = paymentRequestMessagePublisher;
    }

    @Override
    @Transactional
    @Scheduled(
            fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {

        Optional<List<OrderPaymentOutboxMessage>> outboxMessageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.STARTED,
                        SagaStatus.STARTED,
                        SagaStatus.COMPENSATING);

        if (outboxMessageResponse.map(messages -> !messages.isEmpty()).orElse(false)) {
            List<OrderPaymentOutboxMessage> outboxMessages = outboxMessageResponse.get();

            String concatenatedIds = outboxMessages.stream()
                    .map(outboxMessage -> outboxMessage.getId().toString())
                    .collect(Collectors.joining(","));

            log.info(ORDER_ID_CREATING.build(outboxMessages.size(), concatenatedIds));

            outboxMessages.forEach(outboxMessage ->
                    paymentRequestMessagePublisher.publish(
                            outboxMessage,
                            this::updateOutboxStatus));
        }
    }

    private void updateOutboxStatus(OrderPaymentOutboxMessage orderPaymentOutboxMessage, OutboxStatus outboxStatus) {
        orderPaymentOutboxMessage.setOutboxStatus(outboxStatus);
        paymentOutboxHelper.save(orderPaymentOutboxMessage);

        log.info(ORDER_ID_STATUS_UPDATED.build(
                OrderPaymentOutboxMessage.class.getSimpleName(),
                orderPaymentOutboxMessage.getId(),
                outboxStatus.name()));
    }
}
