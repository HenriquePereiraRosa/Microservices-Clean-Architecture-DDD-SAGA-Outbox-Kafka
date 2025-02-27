package com.h.udemy.java.uservices.restaurant.messaging.listener.kafka;

import com.h.udemy.java.uservices.kafka.consumer.KafkaConsumer;
import com.h.udemy.java.uservices.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.h.udemy.java.uservices.restaurant.domain.core.exception.RestaurantDomainException;
import com.h.udemy.java.uservices.restaurant.domain.core.exception.RestaurantNotFoundException;
import com.h.udemy.java.uservices.restaurant.domain.service.RestaurantApprovalRequestMessageListener;
import com.h.udemy.java.uservices.restaurant.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;
import static java.util.Objects.nonNull;

@Slf4j
@Component
public class RestaurantApprovalRequestKafkaListener implements KafkaConsumer<RestaurantApprovalRequestAvroModel> {

    private final String AVRO_MODEL_NAME = RestaurantApprovalRequestAvroModel.class.getSimpleName();
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

        log.info(KAFKA_X_REQUESTS_RECEIVED.build(
                messages.size(),
                AVRO_MODEL_NAME,
                keys.toString(),
                partitions.toString(),
                offsets.toString()));

        messages.forEach(restaurantApprovalRequestAvroModel -> {

            try {
                log.info(KAFKA_PROCESSING_FOR_ID.build(
                        AVRO_MODEL_NAME,
                        restaurantApprovalRequestAvroModel.getOrderId()));

                restaurantApprovalRequestMessageListener.approveOrder(restaurantMessagingDataMapper.
                        restaurantApprovalRequestAvroModelToRestaurantApproval(restaurantApprovalRequestAvroModel));

            } catch (DataAccessException e) {
                SQLException sqlException = (SQLException) e.getCause();
                if (nonNull(sqlException) && !PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                    throw new RestaurantDomainException(ERR_RETHROWN_DATA_ACCESS_EXCEPTION.build(e.getMessage()), e);
                }
                //NO-OP for unique constraint exception
                log.error(ERR_UNIQUE_VIOLATION_IN_REQUEST_LISTENER.build(
                        sqlException.getSQLState(),
                        RestaurantApprovalRequestKafkaListener.class.getSimpleName(),
                        restaurantApprovalRequestAvroModel.getOrderId()));
            } catch (RestaurantNotFoundException e) {
                //NO-OP for PaymentNotFoundException
                log.error(PAYMENT_ERR_ID_NOT_FOUND.build(restaurantApprovalRequestAvroModel.getOrderId()));
            }
        });
    }

}
