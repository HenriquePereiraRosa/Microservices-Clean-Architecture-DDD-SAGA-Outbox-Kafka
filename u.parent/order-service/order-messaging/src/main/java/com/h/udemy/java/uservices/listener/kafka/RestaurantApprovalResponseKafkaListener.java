package com.h.udemy.java.uservices.listener.kafka;

import com.h.udemy.java.uservices.kafka.consumer.KafkaConsumer;
import com.h.udemy.java.uservices.kafka.order.avro.model.OrderApprovalStatus;
import com.h.udemy.java.uservices.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.h.udemy.java.uservices.mapper.OrderMessagingDataMapper;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.restaurantApproval.IRestaurantApprovalMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.h.udemy.java.uservices.domain.log.LogMessages.ORDER_ID_PROCESSED_FAILED;
import static com.h.udemy.java.uservices.domain.log.LogMessages.ORDER_ID_PROCESSED_SUCCESS;
import static com.h.udemy.java.uservices.domain.log.LogMessages.ORDER_KAFKA_NUMBER_MODEL_RESPONSES_RECEIVED;

@Slf4j
@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final String MODEL_NAME = "RESTAURANT APPROVAL";
    private final String KAFKA_CONSUMER_GROUP_ID = "${kafka-consumer-config.restaurant-approval-consumer-group-id}";
    private final String KAFKA_TOPIC_NAME = "${order-service.restaurant-approval-response-topic-name}";
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final IRestaurantApprovalMessageListener restaurantApprovalMessageListener;

    public RestaurantApprovalResponseKafkaListener(OrderMessagingDataMapper orderMessagingDataMapper,
                                                   IRestaurantApprovalMessageListener restaurantApprovalMessageListener) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.restaurantApprovalMessageListener = restaurantApprovalMessageListener;
    }

    @Override
    @KafkaListener(id = KAFKA_CONSUMER_GROUP_ID, topics = KAFKA_TOPIC_NAME)
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
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
            if (OrderApprovalStatus.APPROVED == avroModel.getOrderApprovalStatus()) {

                restaurantApprovalMessageListener.orderApproval(orderMessagingDataMapper
                        .approvalResponseAvroModelToApprovalResponse(avroModel));

                log.info(ORDER_ID_PROCESSED_SUCCESS.get(), avroModel.getOrderId());

            } else if (OrderApprovalStatus.REJECTED == avroModel.getOrderApprovalStatus()) {

                restaurantApprovalMessageListener.orderRejected(orderMessagingDataMapper
                        .approvalResponseAvroModelToApprovalResponse(avroModel));

                log.info(ORDER_ID_PROCESSED_FAILED.get(), avroModel.getOrderId());
            }
        });
    }
}
