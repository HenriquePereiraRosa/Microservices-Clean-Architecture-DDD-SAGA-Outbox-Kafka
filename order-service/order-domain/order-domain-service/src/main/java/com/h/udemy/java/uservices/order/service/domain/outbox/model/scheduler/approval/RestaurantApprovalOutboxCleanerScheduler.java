package com.h.udemy.java.uservices.order.service.domain.outbox.model.scheduler.approval;

import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
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

import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.ORDER_MESSAGES_DELETED;
import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.ORDER_MESSAGES_RECEIVED_FOR_CLEANUP;

@Slf4j
@Component
public class RestaurantApprovalOutboxCleanerScheduler implements OutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final PaymentRequestMessagePublisher paymentRequestMessagePublisher;

    public RestaurantApprovalOutboxCleanerScheduler(
            ApprovalOutboxHelper ApprovalOutboxHelper,
            PaymentRequestMessagePublisher paymentRequestMessagePublisher) {

        this.approvalOutboxHelper = ApprovalOutboxHelper;
        this.paymentRequestMessagePublisher = paymentRequestMessagePublisher;
    }

    @Override
    @Transactional
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {

        Optional<List<OrderApprovalOutboxMessage>> outboxMessageResponse = approvalOutboxHelper
                .getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.COMPLETED,
                        SagaStatus.SUCCEEDED,
                        SagaStatus.COMPENSATED,
                        SagaStatus.FAILED);

        if (outboxMessageResponse.isPresent()) {
            List<OrderApprovalOutboxMessage> outboxMessages = outboxMessageResponse.get();

            String concatenatedMessages = outboxMessages.stream()
                    .map(OrderApprovalOutboxMessage::getPayload)
                    .collect(Collectors.joining("\n"));

            log.info(ORDER_MESSAGES_RECEIVED_FOR_CLEANUP.build(
                    outboxMessages.size(),
                    OrderPaymentOutboxMessage.class.getSimpleName(),
                    concatenatedMessages));

            approvalOutboxHelper
                    .deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(
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
