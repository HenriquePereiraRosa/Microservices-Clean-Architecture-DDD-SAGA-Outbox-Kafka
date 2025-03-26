package com.h.udemy.java.uservices.order.service.message.listener.kafka;

import com.h.udemy.java.uservices.kafka.consumer.KafkaConsumer;
import com.h.udemy.java.uservices.kafka.order.avro.model.CustomerAvroModel;
import com.h.udemy.java.uservices.order.service.message.mapper.OrderMessagingDataMapper;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
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
public class CustomerKafkaListener implements KafkaConsumer<CustomerAvroModel> {

    private final CustomerMessageListener customerMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public CustomerKafkaListener(CustomerMessageListener customerMessageListener,
                                 OrderMessagingDataMapper orderMessagingDataMapper) {
        this.customerMessageListener = customerMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.customer-group-id}",
            topics = "${order-service.customer-topic-name}")
    public void receive(@Payload List<CustomerAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info(KAFKA_X_REQUESTS_RECEIVED.build(
                messages.size(),
                CustomerAvroModel.class.getSimpleName(),
                keys.toString(),
                partitions.toString(),
                offsets.toString()));

        messages.forEach(customerAvroModel ->
                customerMessageListener.customerCreated(orderMessagingDataMapper
                        .customerAvroModeltoCustomerModel(customerAvroModel)));
    }
}
