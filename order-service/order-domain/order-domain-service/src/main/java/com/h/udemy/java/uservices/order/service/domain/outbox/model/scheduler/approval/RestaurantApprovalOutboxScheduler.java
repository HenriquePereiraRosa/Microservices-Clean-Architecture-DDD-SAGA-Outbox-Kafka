package com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.approval;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_STATUS_UPDATED;
import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.ORDER_ID_CREATING;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.h.udemy.java.uservices.outbox.OutboxScheduler;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestaurantApprovalOutboxScheduler implements OutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher;

    public RestaurantApprovalOutboxScheduler(
            ApprovalOutboxHelper approvalOutboxRepository,
            RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher) {

        this.approvalOutboxHelper = approvalOutboxRepository;
        this.restaurantApprovalRequestMessagePublisher = restaurantApprovalRequestMessagePublisher;
    }

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {

        Optional<List<OrderApprovalOutboxMessage>> outboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.STARTED, SagaStatus.PROCESSING);

        if (outboxMessageResponse.map(messages -> !messages.isEmpty()).orElse(false)) {
            List<OrderApprovalOutboxMessage> outboxMessages = outboxMessageResponse.get();

            String concatenatedIds = outboxMessages.stream()
                    .map(outboxMessage -> outboxMessage.getId().toString())
                    .collect(Collectors.joining(","));

            log.info(ORDER_ID_CREATING.build(outboxMessages.size(), concatenatedIds));

            outboxMessages.forEach(outboxMessage ->
                    restaurantApprovalRequestMessagePublisher.publish(
                            outboxMessage,
                            this::updateOutboxStatus));
        }
    }

    private void updateOutboxStatus(OrderApprovalOutboxMessage outboxMessage, OutboxStatus outboxStatus) {
        outboxMessage.setOutboxStatus(outboxStatus);
        approvalOutboxHelper.save(outboxMessage);

        log.info(ORDER_ID_STATUS_UPDATED.build(
                OrderPaymentOutboxMessage.class.getSimpleName(),
                outboxMessage.getId(),
                outboxStatus.name()));
    }
}
