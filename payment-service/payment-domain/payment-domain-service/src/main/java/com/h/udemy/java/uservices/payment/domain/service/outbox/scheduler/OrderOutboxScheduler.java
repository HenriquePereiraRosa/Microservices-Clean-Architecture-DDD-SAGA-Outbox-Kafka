package com.h.udemy.java.uservices.payment.domain.service.outbox.scheduler;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_STATUS_UPDATED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGES_RECEIVED_SENDING_TO_KAFKA;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGES_SENT_TO_MSG_BUS;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.h.udemy.java.uservices.outbox.OutboxScheduler;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.payment.domain.service.outbox.model.OrderOutboxMessage;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.PaymentResponseMessagePublisher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderOutboxScheduler implements OutboxScheduler {

    public static final String OUTBOX_MESSAGE_CLASS_NAME = OrderOutboxMessage.class.getSimpleName();
    private final OrderOutboxHelper orderOutboxHelper;
    private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;

    public OrderOutboxScheduler(
            OrderOutboxHelper orderOutboxHelper,
            PaymentResponseMessagePublisher paymentResponseMessagePublisher) {
        this.orderOutboxHelper = orderOutboxHelper;
        this.paymentResponseMessagePublisher = paymentResponseMessagePublisher;
    }

    @Override
    @Transactional
    @Scheduled(
            fixedDelayString = "${payment-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${payment-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {

        Optional<List<OrderOutboxMessage>> outboxMessageResponse =
                orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(
                        OutboxStatus.STARTED);

        if (outboxMessageResponse.map(messages -> !messages.isEmpty()).orElse(false)) {
            List<OrderOutboxMessage> outboxMessages = outboxMessageResponse.get();

            String concatenatedIds = outboxMessages.stream()
                    .map(outboxMessage -> outboxMessage.getId().toString())
                    .collect(Collectors.joining(","));

            log.info(OUTBOX_MESSAGES_RECEIVED_SENDING_TO_KAFKA.build(
                    outboxMessages.size(),
                    OUTBOX_MESSAGE_CLASS_NAME,
                    concatenatedIds));

            outboxMessages.forEach(outboxMessage ->
                    paymentResponseMessagePublisher.publish(
                            outboxMessage,
                            orderOutboxHelper::updateOutboxStatus));
            log.info(OUTBOX_MESSAGES_SENT_TO_MSG_BUS.build(
                    outboxMessages.size(),
                    OUTBOX_MESSAGE_CLASS_NAME
            ));
        }
    }
}
