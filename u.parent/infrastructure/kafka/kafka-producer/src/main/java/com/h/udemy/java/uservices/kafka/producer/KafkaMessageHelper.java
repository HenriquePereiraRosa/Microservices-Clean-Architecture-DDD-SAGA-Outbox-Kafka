package com.h.udemy.java.uservices.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ERROR_WHILE_SENDING_REQUEST_AVRO;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_SUCCESSFUL_RESPONSE_KAFKA;

@Slf4j
@Component
public class KafkaMessageHelper {

    public <T> ListenableFutureCallback<SendResult<String, T>>
        getKafkaCallback(String topicName,
                         T avroModel,
                         final String avroModelName,
                         final String orderId) {
        return new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error(ORDER_ERROR_WHILE_SENDING_REQUEST_AVRO.build(avroModelName,
                        avroModel.toString(),
                        topicName,
                        ex));
            }

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metadata = result.getRecordMetadata();

                log.info(ORDER_SUCCESSFUL_RESPONSE_KAFKA.build(orderId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp()));
            }
        };
    }

}
