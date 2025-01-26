package com.h.udemy.java.uservices.listener.kafka;

import com.h.udemy.java.uservices.kafka.consumer.IKafkaConsumer;
import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentResponseAvroModel;
import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentStatus;
import com.h.udemy.java.uservices.mapper.OrderMessagingDataMapper;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_PROCESSED_FAILED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_PROCESSED_SUCCESS;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_KAFKA_NUMBER_MODEL_RESPONSES_RECEIVED;

@Slf4j
@Component
public class PaymentResponseKafkaListener implements IKafkaConsumer<PaymentResponseAvroModel> {

    private final String MODEL_NAME = "PAYMENT";
    private final String KAFKA_CONSUMER_GROUP_ID = "${kafka-consumer-config.payment-consumer-group-id}";
    private final String KAFKA_TOPIC_NAME = "${order-service.payment-response-topic-name}";
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final PaymentResponseMessageListener paymentResponseMessageListener;

    public PaymentResponseKafkaListener(OrderMessagingDataMapper orderMessagingDataMapper,
                                        PaymentResponseMessageListener paymentResponseMessageListener) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.paymentResponseMessageListener = paymentResponseMessageListener;
    }

    @Override
    @KafkaListener(id = KAFKA_CONSUMER_GROUP_ID, topics = KAFKA_TOPIC_NAME)
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info(ORDER_KAFKA_NUMBER_MODEL_RESPONSES_RECEIVED.build(
                messages.size(),
                MODEL_NAME,
                keys.toString(),
                partitions.toString(),
                offsets.toString()));

        messages.forEach(avroModel -> {
            if (PaymentStatus.COMPLETED == avroModel.getPaymentStatus()) {

                paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper
                        .paymentResponseAvroModelToPaymentResponse(avroModel));

                log.info(ORDER_ID_PROCESSED_SUCCESS.build(avroModel.getOrderId()));

            } else if (PaymentStatus.CANCELLED == avroModel.getPaymentStatus()
                    || PaymentStatus.FAILED == avroModel.getPaymentStatus()) {

                paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper
                        .paymentResponseAvroModelToPaymentResponse(avroModel));

                log.info(ORDER_ID_PROCESSED_FAILED.build(avroModel.getOrderId()));
            }
        });
    }
}
