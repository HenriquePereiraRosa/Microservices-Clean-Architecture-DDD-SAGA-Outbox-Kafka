package com.h.udemy.java.uservices.restaurant.messaging.listener.kafka;

import com.h.udemy.java.uservices.kafka.consumer.IKafkaConsumer;
import com.h.udemy.java.uservices.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.h.udemy.java.uservices.restaurant.domain.service.RestaurantApprovalRequestMessageListener;
import com.h.udemy.java.uservices.restaurant.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.KAFKA_PROCESSING_FOR_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.KAFKA_X_REQUESTS_RECEIVED;

@Slf4j
@Component
public class RestaurantApprovalRequestKafkaListener implements IKafkaConsumer<RestaurantApprovalRequestAvroModel> {

    private static final String REQUEST_SERVICE_NAME = "Order Approval";
    private static final String SERVICE_NAME = "Restaurant Approval";
    private final RestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener;
    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;

    public RestaurantApprovalRequestKafkaListener(RestaurantApprovalRequestMessageListener
                                                          restaurantApprovalRequestMessageListener,
                                                  RestaurantMessagingDataMapper
                                                          restaurantMessagingDataMapper) {
        this.restaurantApprovalRequestMessageListener = restaurantApprovalRequestMessageListener;
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${restaurant-service.restaurant-approval-request-topic-name}")
    public void receive(@Payload List<RestaurantApprovalRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info(KAFKA_X_REQUESTS_RECEIVED.build(REQUEST_SERVICE_NAME,
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                SERVICE_NAME));

        messages.forEach(restaurantApprovalRequestAvroModel -> {
            log.info(KAFKA_PROCESSING_FOR_ID.build(
                    REQUEST_SERVICE_NAME,
                    restaurantApprovalRequestAvroModel.getOrderId()));

            restaurantApprovalRequestMessageListener.approveOrder(restaurantMessagingDataMapper.
                    restaurantApprovalRequestAvroModelToRestaurantApproval(restaurantApprovalRequestAvroModel));
        });
    }

}
