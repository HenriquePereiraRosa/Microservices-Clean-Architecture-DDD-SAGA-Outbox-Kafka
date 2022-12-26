package com.h.udemy.java.uservices.listener.kafka;

import com.h.udemy.java.uservices.kafka.consumer.KafkaConsumer;
import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentResponseAvroModel;
import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentStatus;
import com.h.udemy.java.uservices.mapper.OrderMessagingDataMapper;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.payment.IPaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_PROCESSED_FAILED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_PROCESSED_SUCCESS;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_KAFKA_NUMBER_MODEL_RESPONSES_RECEIVED;

@Slf4j
@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final String MODEL_NAME = "PAYMENT";
    private final String KAFKA_CONSUMER_GROUP_ID = "${kafka-consumer-config.payment-consumer-group-id}";
    private final String KAFKA_TOPIC_NAME = "${order-service.payment-response-topic-name}";
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final IPaymentResponseMessageListener paymentResponseMessageListener;

    public PaymentResponseKafkaListener(OrderMessagingDataMapper orderMessagingDataMapper,
                                        IPaymentResponseMessageListener paymentResponseMessageListener) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.paymentResponseMessageListener = paymentResponseMessageListener;
    }

    @Override
    @KafkaListener(id = KAFKA_CONSUMER_GROUP_ID, topics = KAFKA_TOPIC_NAME)
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header List<String> keys,
                        @Header List<Integer> partitions,
                        @Header List<Long> offsets) {
        log.info(ORDER_KAFKA_NUMBER_MODEL_RESPONSES_RECEIVED.get(),
                messages.size(),
                MODEL_NAME,
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(avroModel -> {
            if (PaymentStatus.COMPLETED == avroModel.getPaymentStatus()) {

                paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper
                        .paymentResponseAvroModelToPaymentResponse(avroModel));

                log.info(ORDER_ID_PROCESSED_SUCCESS.get(), avroModel.getOrderId());

            } else if (PaymentStatus.CANCELLED == avroModel.getPaymentStatus()
                    || PaymentStatus.FAILED == avroModel.getPaymentStatus()) {

                paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper
                        .paymentResponseAvroModelToPaymentResponse(avroModel));

                log.info(ORDER_ID_PROCESSED_FAILED.get(), avroModel.getOrderId());
            }
        });
    }
}
