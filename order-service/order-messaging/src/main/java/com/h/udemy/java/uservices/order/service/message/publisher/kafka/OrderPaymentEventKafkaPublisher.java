package com.h.udemy.java.uservices.order.service.message.publisher.kafka;

import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentRequestAvroModel;
import com.h.udemy.java.uservices.kafka.producer.KafkaMessageHelper;
import com.h.udemy.java.uservices.kafka.producer.service.impl.KafkaProducerI;
import com.h.udemy.java.uservices.order.service.message.mapper.OrderMessagingDataMapper;
import com.h.udemy.java.uservices.order.service.domain.config.OrderServiceConfigData;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.h.udemy.java.uservices.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;

@Slf4j
@Component
public class OrderPaymentEventKafkaPublisher implements PaymentRequestMessagePublisher {

    private static final String OUTBOX_MESSAGE_CLASS_NAME = OrderApprovalOutboxMessage.class.getSimpleName();
    private static final String AVRO_MODEL_NAME = PaymentRequestAvroModel.class.getSimpleName();

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducerI<String, PaymentRequestAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderPaymentEventKafkaPublisher(
            OrderMessagingDataMapper orderMessagingDataMapper,
            OrderServiceConfigData orderServiceConfigData,
            KafkaProducerI<String, PaymentRequestAvroModel> kafkaProducer,
            KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(
            OrderPaymentOutboxMessage orderPaymentOutboxMessage,
            BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback) {

        OrderPaymentEventPayload orderPaymentEventPayload =
                kafkaMessageHelper.getOrderEventPayload(
                        OrderPaymentEventPayload.class,
                        orderPaymentOutboxMessage.getPayload());

        String sagaId = orderPaymentOutboxMessage.getSagaId().toString();

        log.info(OUTBOX_MESSAGE_RECEIVED.build(
                OrderPaymentOutboxMessage.class.getSimpleName(),
                orderPaymentEventPayload.getOrderId(),
                sagaId));

        try {
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper
                    .orderPaymentEventToPaymentRequestAvroModel(
                            sagaId,
                            orderPaymentEventPayload);
            kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    sagaId,
                    paymentRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getPaymentRequestTopicName(),
                            paymentRequestAvroModel,
                            orderPaymentOutboxMessage,
                            outboxCallback,
                            AVRO_MODEL_NAME,
                            orderPaymentEventPayload.getOrderId()));

            log.info(EVENT_SENT_TO_KAFKA.build(
                    OUTBOX_MESSAGE_CLASS_NAME,
                    orderPaymentEventPayload.getOrderId(),
                    sagaId));

        } catch (Exception e) {
            log.error(EVENT_ERR_SENT_TO_KAFKA.build(
                            OUTBOX_MESSAGE_CLASS_NAME,
                            AVRO_MODEL_NAME,
                            orderPaymentEventPayload.getOrderId(),
                            sagaId,
                            e.getMessage()),
                    e);
        }
    }
}
