package com.h.udemy.java.uservices.messaging.kafka;

import com.h.udemy.java.uservices.core.entity.Customer;
import com.h.udemy.java.uservices.core.event.CustomerCreatedEvent;
import com.h.udemy.java.uservices.kafka.order.avro.model.CustomerAvroModel;
import com.h.udemy.java.uservices.kafka.producer.service.KafkaProducer;
import com.h.udemy.java.uservices.messaging.mapper.CustomerMessagingDataMapper;
import com.h.udemy.java.uservices.service.config.CustomerServiceConfigData;
import com.h.udemy.java.uservices.service.ports.output.message.publisher.CustomerMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;

@Slf4j
@Component
public class CustomerCreatedEventKafkaPublisher implements CustomerMessagePublisher {

    private final CustomerMessagingDataMapper customerMessagingDataMapper;

    private final KafkaProducer<String, CustomerAvroModel> kafkaProducer;

    private final CustomerServiceConfigData customerServiceConfigData;

    public CustomerCreatedEventKafkaPublisher(CustomerMessagingDataMapper customerMessagingDataMapper,
                                              KafkaProducer<String, CustomerAvroModel> kafkaProducer,
                                              CustomerServiceConfigData customerServiceConfigData) {
        this.customerMessagingDataMapper = customerMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.customerServiceConfigData = customerServiceConfigData;
    }

    @Override
    public void publish(CustomerCreatedEvent customerCreatedEvent) {
        log.info(EVENT_RECEIVED.build(
                CustomerCreatedEvent.class.getSimpleName(),
                Customer.class.getSimpleName(),
                customerCreatedEvent.getCustomer().getId().getValue()));

        try {
            CustomerAvroModel customerAvroModel = customerMessagingDataMapper
                    .paymentResponseAvroModelToPaymentResponse(customerCreatedEvent);

            kafkaProducer.send(customerServiceConfigData.getCustomerTopicName(), customerAvroModel.getId(),
                    customerAvroModel,
                    getCallback(customerServiceConfigData.getCustomerTopicName(), customerAvroModel));

            log.info(EVENT_X_ID_SENT_TO_KAFKA.build(
                    CustomerCreatedEvent.class.getSimpleName(),
                    Customer.class.getSimpleName(),
                    customerAvroModel.getId()));

        } catch (Exception e) {
            log.error(CUSTOMER_EVENT_ERR_SENDING_CUSTOMER_TO_KAFKA.build(
                    CustomerCreatedEvent.class.getSimpleName(),
                    Customer.class.getSimpleName(),
                    customerCreatedEvent.getCustomer().getId().getValue(),
                    e.getMessage()));
        }
    }

    private ListenableFutureCallback<SendResult<String, CustomerAvroModel>>
    getCallback(String topicName, CustomerAvroModel message) {

        return new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error(CUSTOMER_EVENT_ERR_SENDING_TO_KAFKA.build(
                                message.toString(),
                                topicName),
                        throwable);
            }

            @Override
            public void onSuccess(SendResult<String, CustomerAvroModel> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info(CUSTOMER_EVENT_ON_SUCCESS.build(
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime()));
            }
        };
    }
}
