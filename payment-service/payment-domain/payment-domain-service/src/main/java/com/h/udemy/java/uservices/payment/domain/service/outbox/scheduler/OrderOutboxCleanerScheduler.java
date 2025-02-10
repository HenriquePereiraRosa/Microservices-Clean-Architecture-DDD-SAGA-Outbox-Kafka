package com.h.udemy.java.uservices.payment.domain.service.outbox.scheduler;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_MESSAGES_DELETED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_MESSAGES_RECEIVED_FOR_CLEANUP;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.h.udemy.java.uservices.outbox.OutboxScheduler;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.payment.domain.service.outbox.model.OrderOutboxMessage;
import com.h.udemy.java.uservices.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderOutboxCleanerScheduler implements OutboxScheduler {

    public static final String OUTBOX_MESSAGE_CLASS_NAME = OrderOutboxMessage.class.getSimpleName();
    private final OrderOutboxHelper orderOutboxHelper;

    public OrderOutboxCleanerScheduler(
            OrderOutboxHelper orderOutboxHelper) {

        this.orderOutboxHelper = orderOutboxHelper;
    }

    @Override
    @Transactional
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {

        Optional<List<OrderOutboxMessage>> outboxMessageResponse = orderOutboxHelper
                .getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);

        if (outboxMessageResponse.map(messages -> !messages.isEmpty()).orElse(false)) {
            List<OrderOutboxMessage> outboxMessages = outboxMessageResponse.get();

            String concatenatedMessages = outboxMessages.stream()
                    .map(OrderOutboxMessage::getPayload)
                    .collect(Collectors.joining("\n"));

            log.info(ORDER_MESSAGES_RECEIVED_FOR_CLEANUP.build(
                    outboxMessages.size(),
                    OUTBOX_MESSAGE_CLASS_NAME,
                    concatenatedMessages));

            orderOutboxHelper
                    .deleteOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);

            log.info(ORDER_MESSAGES_DELETED.build(
                    outboxMessages.size(),
                    OUTBOX_MESSAGE_CLASS_NAME,
                    concatenatedMessages));
        }
    }
}
