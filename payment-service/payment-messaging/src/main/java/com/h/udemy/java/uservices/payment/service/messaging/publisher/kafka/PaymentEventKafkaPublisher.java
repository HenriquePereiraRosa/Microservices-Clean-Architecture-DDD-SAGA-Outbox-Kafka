package com.h.udemy.java.uservices.payment.service.messaging.publisher.kafka;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.EVENT_ERR_SENT_TO_KAFKA;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.EVENT_SENT_TO_KAFKA;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.OUTBOX_MESSAGE_RECEIVED;

import java.util.function.BiConsumer;

import org.springframework.stereotype.Component;

import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentResponseAvroModel;
import com.h.udemy.java.uservices.kafka.producer.KafkaMessageHelper;
import com.h.udemy.java.uservices.kafka.producer.service.KafkaProducer;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.payment.domain.service.config.PaymentServiceConfigData;
import com.h.udemy.java.uservices.payment.domain.service.outbox.model.OrderEventPayload;
import com.h.udemy.java.uservices.payment.domain.service.outbox.model.OrderOutboxMessage;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.h.udemy.java.uservices.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentEventKafkaPublisher implements PaymentResponseMessagePublisher {

    private static final String OUTBOX_MESSAGE_CLASS_NAME = OrderOutboxMessage.class.getSimpleName();
    public static final String AVRO_MODEL_NAME = PaymentResponseAvroModel.class.getSimpleName();
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public PaymentEventKafkaPublisher(PaymentMessagingDataMapper paymentMessagingDataMapper,
                                      KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
                                      PaymentServiceConfigData paymentServiceConfigData,
                                      KafkaMessageHelper kafkaMessageHelper) {
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.paymentServiceConfigData = paymentServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage,
                        BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
        OrderEventPayload orderEventPayload =
                kafkaMessageHelper.getOrderEventPayload(OrderEventPayload.class, orderOutboxMessage.getPayload());

        String sagaId = orderOutboxMessage.getSagaId().toString();

        log.info(OUTBOX_MESSAGE_RECEIVED.build(
                OrderOutboxMessage.class.getSimpleName(),
                orderEventPayload.getOrderId(),
                sagaId));

        try {
            PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper
                    .orderEventPayloadToPaymentResponseAvroModel(sagaId, orderEventPayload);

            kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
                    sagaId,
                    paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(paymentServiceConfigData.getPaymentResponseTopicName(),
                            paymentResponseAvroModel,
                            orderOutboxMessage,
                            outboxCallback,
                            orderEventPayload.getOrderId(),
                            AVRO_MODEL_NAME));

            log.info(EVENT_SENT_TO_KAFKA.build(
                    OUTBOX_MESSAGE_CLASS_NAME,
                    orderEventPayload.getOrderId(),
                    sagaId));

        } catch (Exception e) {
            log.error(EVENT_ERR_SENT_TO_KAFKA.build(
                            OUTBOX_MESSAGE_CLASS_NAME,
                            AVRO_MODEL_NAME,
                            orderEventPayload.getOrderId(),
                            sagaId,
                            e.getMessage()),
                    e);
        }
    }
}
