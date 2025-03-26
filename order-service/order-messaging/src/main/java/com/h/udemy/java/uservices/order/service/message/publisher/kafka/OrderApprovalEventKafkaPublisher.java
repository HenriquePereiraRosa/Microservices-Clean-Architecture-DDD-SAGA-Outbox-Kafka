package com.h.udemy.java.uservices.order.service.message.publisher.kafka;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.EVENT_ERR_SENT_TO_KAFKA;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.EVENT_SENT_TO_KAFKA;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_RECEIVED;

import java.util.function.BiConsumer;

import com.h.udemy.java.uservices.kafka.producer.service.KafkaProducer;
import com.h.udemy.java.uservices.order.service.message.mapper.OrderMessagingDataMapper;
import org.springframework.stereotype.Component;

import com.h.udemy.java.uservices.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.h.udemy.java.uservices.kafka.producer.KafkaMessageHelper;
import com.h.udemy.java.uservices.kafka.producer.service.impl.KafkaProducerI;
import com.h.udemy.java.uservices.order.service.domain.config.OrderServiceConfigData;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderApprovalEventKafkaPublisher implements RestaurantApprovalRequestMessagePublisher {

    private static final String OUTBOX_MESSAGE_CLASS_NAME = OrderApprovalOutboxMessage.class.getSimpleName();
    private static final String AVRO_MODEL_NAME = RestaurantApprovalRequestAvroModel.class.getSimpleName();

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderApprovalEventKafkaPublisher(
            OrderMessagingDataMapper orderMessagingDataMapper,
            OrderServiceConfigData orderServiceConfigData,
            KafkaProducerI<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
            KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(
            OrderApprovalOutboxMessage orderApprovalOutboxMessage,
            BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback) {

        OrderApprovalEventPayload orderApprovalEventPayload =
                kafkaMessageHelper.getOrderEventPayload(
                        OrderApprovalEventPayload.class,
                        orderApprovalOutboxMessage.getPayload());

        String sagaId = orderApprovalOutboxMessage.getSagaId().toString();

        log.info(OUTBOX_MESSAGE_RECEIVED.build(
                OUTBOX_MESSAGE_CLASS_NAME,
                orderApprovalEventPayload.getOrderId(),
                sagaId));

        try {
            RestaurantApprovalRequestAvroModel approvalRequestAvroModel = orderMessagingDataMapper
                    .orderApprovalEventToRestaurantApprovalRequestAvroModel(
                            sagaId,
                            orderApprovalEventPayload);

            kafkaProducer.send(
                    orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    sagaId,
                    approvalRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            approvalRequestAvroModel,
                            orderApprovalOutboxMessage,
                            outboxCallback,
                            AVRO_MODEL_NAME,
                            orderApprovalEventPayload.getOrderId()));

            log.info(EVENT_SENT_TO_KAFKA.build(
                    OrderApprovalEventPayload.class.getSimpleName(),
                    orderApprovalEventPayload.getOrderId(),
                    sagaId));

        } catch (Exception e) {
            log.error(EVENT_ERR_SENT_TO_KAFKA.build(
                            OUTBOX_MESSAGE_CLASS_NAME,
                            AVRO_MODEL_NAME,
                            orderApprovalEventPayload.getOrderId(),
                            sagaId,
                            e.getMessage()),
                    e);
        }
    }
}
