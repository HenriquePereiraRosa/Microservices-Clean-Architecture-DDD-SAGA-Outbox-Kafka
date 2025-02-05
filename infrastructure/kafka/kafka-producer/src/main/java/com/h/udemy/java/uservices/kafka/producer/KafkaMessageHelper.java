package com.h.udemy.java.uservices.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderDomainException;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.function.BiConsumer;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;

@Slf4j
@Component
public class KafkaMessageHelper {

    private final ObjectMapper objectMapper;

    public KafkaMessageHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T getOrderEventPayload(Class<T> outputType, String payload) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (JsonProcessingException e) {
            final String eMessage = OUTBOX_MESSAGE_COULD_NOT_BE_READ.build(outputType.getSimpleName());
            log.error(eMessage, e);
            throw new OrderDomainException(eMessage, e);
        }
    }

    public <T, U> ListenableFutureCallback<SendResult<String, T>>
        getKafkaCallback(String topicName,
                         T avroModel,
                         U outboxMessage,
                         BiConsumer<U, OutboxStatus> outboxCallback,
                         final String avroModelName,
                         final String orderId) {

        return new ListenableFutureCallback<SendResult<String, T>>() {

            @Override
            public void onFailure(Throwable ex) {

                log.error(ORDER_ERROR_WHILE_SENDING_REQUEST_AVRO.build(avroModelName,
                        avroModel.toString(),
                        topicName,
                        ex));

                outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
            }

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metadata = result.getRecordMetadata();

                log.info(ORDER_SUCCESSFUL_RESPONSE_KAFKA.build(orderId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp()));

                outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);
            }
        };
    }

}
