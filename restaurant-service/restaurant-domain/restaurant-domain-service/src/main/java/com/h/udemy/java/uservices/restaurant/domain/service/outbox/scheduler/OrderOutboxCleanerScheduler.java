package com.h.udemy.java.uservices.restaurant.domain.service.outbox.scheduler;

import com.h.udemy.java.uservices.outbox.OutboxScheduler;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.restaurant.domain.service.outbox.model.OrderOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_MESSAGES_DELETED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_MESSAGES_RECEIVED_FOR_CLEANUP;

@Slf4j
@Component
public class OrderOutboxCleanerScheduler implements OutboxScheduler {

    public static final String OUTBOX_MSG_CLASS_NAME = OrderOutboxMessage.class.getSimpleName();

    private final OrderOutboxHelper orderOutboxHelper;

    public OrderOutboxCleanerScheduler(OrderOutboxHelper orderOutboxHelper) {
        this.orderOutboxHelper = orderOutboxHelper;
    }

    @Transactional
    @Scheduled(cron = "@midnight")
    @Override
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> outboxMessagesResponse =
                orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);

        if (outboxMessagesResponse.map(m -> !m.isEmpty()).orElse(false)) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();

            String concatenatedMessages = outboxMessagesResponse.get().stream()
                    .map(OrderOutboxMessage::getPayload)
                    .reduce((msg1, msg2) -> msg1 + "\n" + msg2)
                    .orElse("");

            log.info(ORDER_MESSAGES_RECEIVED_FOR_CLEANUP.build(
                    outboxMessages.size(),
                    OUTBOX_MSG_CLASS_NAME,
                    concatenatedMessages));

            orderOutboxHelper.deleteOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);

            log.info(ORDER_MESSAGES_DELETED.build(
                    outboxMessages.size(),
                    OUTBOX_MSG_CLASS_NAME,
                    concatenatedMessages));
        }
    }
}