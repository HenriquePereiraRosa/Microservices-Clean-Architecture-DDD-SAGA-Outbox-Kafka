package com.h.udemy.java.uservices.restaurant.messaging.publisher.kafka;

import com.h.udemy.java.uservices.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.h.udemy.java.uservices.kafka.producer.KafkaMessageHelper;
import com.h.udemy.java.uservices.kafka.producer.service.impl.KafkaProducerI;
import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderApprovedEvent;
import com.h.udemy.java.uservices.restaurant.domain.service.config.RestaurantServiceConfigData;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.h.udemy.java.uservices.restaurant.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.EVENT_ERR_SENT_TO_KAFKA;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.EVENT_RECEIVED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.EVENT_SENT_TO_KAFKA_AT;

@Slf4j
@Component
public class OrderApprovedKafkaMessagePublisher implements OrderApprovedMessagePublisher {

    public static final String AVRO_MODEL_NAME = "RestaurantApprovalResponseAvroModel";
    public static final String TARGET_NAME = "OrderId";
    public static final String EVENT_NAME = "OrderApprovedEvent";

    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final KafkaProducerI<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderApprovedKafkaMessagePublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper,
                                              KafkaProducerI<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
                                              RestaurantServiceConfigData restaurantServiceConfigData,
                                              KafkaMessageHelper kafkaMessageHelper) {
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderApprovedEvent orderApprovedEvent) {
        String orderId = orderApprovedEvent.getOrderApproval().getOrderId().getValue().toString();

        log.info(EVENT_RECEIVED.build(EVENT_NAME, TARGET_NAME, orderId));

        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel =
                    restaurantMessagingDataMapper
                            .orderApprovedEventToRestaurantApprovalResponseAvroModel(orderApprovedEvent);
// todo
//            kafkaProducer.send(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
//                    orderId,
//                    restaurantApprovalResponseAvroModel,
//                    kafkaMessageHelper.getKafkaCallback(restaurantServiceConfigData
//                                    .getRestaurantApprovalResponseTopicName(),
//                            restaurantApprovalResponseAvroModel,
//                            orderId,
//                            AVRO_MODEL_NAME));

            log.info(EVENT_SENT_TO_KAFKA_AT.build(AVRO_MODEL_NAME, System.nanoTime()));

        } catch (Exception e) {
            log.info(EVENT_ERR_SENT_TO_KAFKA.build(AVRO_MODEL_NAME, TARGET_NAME, orderId, e.getMessage()));
        }
    }

}
