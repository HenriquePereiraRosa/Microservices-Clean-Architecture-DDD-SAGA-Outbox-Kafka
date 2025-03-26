package com.h.udemy.java.uservices.order.service.message.listener.kafka;

import com.h.udemy.java.uservices.kafka.consumer.KafkaConsumer;
import com.h.udemy.java.uservices.kafka.order.avro.model.OrderApprovalStatus;
import com.h.udemy.java.uservices.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.h.udemy.java.uservices.order.service.message.mapper.OrderMessagingDataMapper;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderNotFoundException;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.restaurantApproval.IRestaurantApprovalMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;

@Slf4j
@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final IRestaurantApprovalMessageListener restaurantApprovalMessageListener;

    public RestaurantApprovalResponseKafkaListener(OrderMessagingDataMapper orderMessagingDataMapper,
                                                   IRestaurantApprovalMessageListener restaurantApprovalMessageListener) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.restaurantApprovalMessageListener = restaurantApprovalMessageListener;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info(KAFKA_X_REQUESTS_RECEIVED.build(
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
                log.error(ERR_NOT_FOUND.build(
                        Order.class.getSimpleName(),
                        avroModel.getOrderId()));
            }
        });
    }
}
