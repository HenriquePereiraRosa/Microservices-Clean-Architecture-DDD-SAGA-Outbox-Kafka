package com.h.udemy.java.uservices.payment.service.messaging.listener.kafka;

import com.h.udemy.java.uservices.kafka.consumer.IKafkaConsumer;
import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentRequestAvroModel;
import com.h.udemy.java.uservices.payment.domain.service.PaymentRequestMessageListener;
import com.h.udemy.java.uservices.payment.service.messaging.listener.kafka.strategy.PaymentProcessor;
import com.h.udemy.java.uservices.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.KAFKA_X_REQUESTS_RECEIVED;

@Slf4j
@Component
public class PaymentRequestKafkaListener implements IKafkaConsumer<PaymentRequestAvroModel> {

    private final String KAFKA_CONSUMER_GROUP_ID = "${kafka-consumer-config.payment-consumer-group-id}";
    private final String KAFKA_TOPIC_NAME = "${payment-service.payment-request-topic-name}";

    private final PaymentRequestMessageListener listener;
    private final PaymentProcessor paymentProcessor;

    public PaymentRequestKafkaListener(PaymentRequestMessageListener paymentRequestMessageListener,
                                       PaymentMessagingDataMapper mapper) {
        this.listener = paymentRequestMessageListener;
        this.paymentProcessor = new PaymentProcessor(listener, mapper);
    }

    @Override
    @KafkaListener(id = KAFKA_CONSUMER_GROUP_ID, topics = KAFKA_TOPIC_NAME)
    public void receive(@Payload List<PaymentRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offset) {
        log.info(KAFKA_X_REQUESTS_RECEIVED.build(
                messages.size(),
                keys.toString(),
                offset.toString()));

        messages.forEach(paymentProcessor::processPayment);

    }
}
