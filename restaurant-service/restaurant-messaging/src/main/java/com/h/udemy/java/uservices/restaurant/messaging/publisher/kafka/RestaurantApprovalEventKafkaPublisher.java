package com.h.udemy.java.uservices.restaurant.messaging.publisher.kafka;

import com.h.udemy.java.uservices.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.h.udemy.java.uservices.kafka.producer.KafkaMessageHelper;
import com.h.udemy.java.uservices.kafka.producer.service.KafkaProducer;
import com.h.udemy.java.uservices.outbox.OutboxStatus;
import com.h.udemy.java.uservices.restaurant.domain.service.config.RestaurantServiceConfigData;
import com.h.udemy.java.uservices.restaurant.domain.service.outbox.model.OrderEventPayload;
import com.h.udemy.java.uservices.restaurant.domain.service.outbox.model.OrderOutboxMessage;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
import com.h.udemy.java.uservices.restaurant.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;

@Slf4j
@Component
public class RestaurantApprovalEventKafkaPublisher implements RestaurantApprovalResponseMessagePublisher {

    private static final String OUTBOX_MESSAGE_CLASS_NAME = OrderOutboxMessage.class.getSimpleName();
    public static final String AVRO_MODEL_NAME = RestaurantApprovalResponseAvroModel.class.getSimpleName();

    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public RestaurantApprovalEventKafkaPublisher(RestaurantMessagingDataMapper dataMapper,
                                                 KafkaProducer<String, RestaurantApprovalResponseAvroModel>
                                                         kafkaProducer,
                                                 RestaurantServiceConfigData restaurantServiceConfigData,
                                                 KafkaMessageHelper kafkaMessageHelper) {
        this.restaurantMessagingDataMapper = dataMapper;
        this.kafkaProducer = kafkaProducer;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }


    @Override
    public void publish(OrderOutboxMessage orderOutboxMessage,
                        BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {
        OrderEventPayload orderEventPayload =
                kafkaMessageHelper.getOrderEventPayload(
                        OrderEventPayload.class,
                        orderOutboxMessage.getPayload());

        String sagaId = orderOutboxMessage.getSagaId().toString();

        log.info(OUTBOX_MESSAGE_RECEIVED.build(
                OrderOutboxMessage.class.getSimpleName(),
                orderEventPayload.getOrderId(),
                sagaId));
        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel =
                    restaurantMessagingDataMapper
                            .orderEventPayloadToRestaurantApprovalResponseAvroModel(sagaId, orderEventPayload);

            kafkaProducer.send(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                    sagaId,
                    restaurantApprovalResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(restaurantServiceConfigData
                                    .getRestaurantApprovalResponseTopicName(),
                            restaurantApprovalResponseAvroModel,
                            orderOutboxMessage,
                            outboxCallback,
                            orderEventPayload.getOrderId(),
                            AVRO_MODEL_NAME));

            log.info(EVENT_SENT_TO_KAFKA.build(
                    OUTBOX_MESSAGE_CLASS_NAME,
                    restaurantApprovalResponseAvroModel.getOrderId(),
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
