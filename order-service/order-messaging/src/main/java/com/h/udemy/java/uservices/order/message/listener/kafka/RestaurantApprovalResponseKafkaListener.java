package com.h.udemy.java.uservices.order.message.listener.kafka;

import com.h.udemy.java.uservices.kafka.consumer.IKafkaConsumer;
import com.h.udemy.java.uservices.kafka.order.avro.model.OrderApprovalStatus;
import com.h.udemy.java.uservices.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.h.udemy.java.uservices.order.message.mapper.OrderMessagingDataMapper;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderNotFoundException;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.restaurantApproval.IRestaurantApprovalMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;

@Slf4j
@Component
public class RestaurantApprovalResponseKafkaListener implements IKafkaConsumer<RestaurantApprovalResponseAvroModel> {

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
        log.info(ORDER_KAFKA_NUMBER_MODEL_RESPONSES_RECEIVED.build(
                messages.size(),
                RestaurantApprovalResponseAvroModel.class.getSimpleName(),
                keys.toString(),
                partitions.toString(),
                offsets.toString()));

        messages.forEach(avroModel -> {
            try {
                if (OrderApprovalStatus.APPROVED == avroModel.getOrderApprovalStatus()) {

                    restaurantApprovalMessageListener.orderApproval(orderMessagingDataMapper
                            .approvalResponseAvroModelToApprovalResponse(avroModel));

                    log.info(ORDER_ID_PROCESSED_SUCCESS.build(avroModel.getOrderId()));

                } else if (OrderApprovalStatus.REJECTED == avroModel.getOrderApprovalStatus()) {

                    restaurantApprovalMessageListener.orderRejected(orderMessagingDataMapper
                            .approvalResponseAvroModelToApprovalResponse(avroModel));

                    log.info(ORDER_ID_PROCESSED_FAILED.build(avroModel.getOrderId()));
                }

            } catch (OptimisticLockingFailureException e) {
                // NO-OP for optimistic lock. This means another thread finished the work,
                // so do not throw error to prevent reading the data from kafka again!
                log.error(EVENT_ERR_OPTIMISTIC_LOCK.build(
                        PaymentResponseMessageListener.class.getSimpleName(),
                        avroModel.getOrderId()));
            } catch (OrderNotFoundException e) {
                //NO-OP for OrderNotFoundException
                log.error(ORDER_ERROR_NOT_FOUND.build(avroModel.getOrderId()));
            }
        });
    }
}
