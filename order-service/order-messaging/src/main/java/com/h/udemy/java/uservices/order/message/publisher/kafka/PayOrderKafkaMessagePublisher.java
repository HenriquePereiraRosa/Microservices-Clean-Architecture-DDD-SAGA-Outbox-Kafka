package com.h.udemy.java.uservices.order.message.publisher.kafka;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.h.udemy.java.uservices.kafka.producer.KafkaMessageHelper;
import com.h.udemy.java.uservices.kafka.producer.service.impl.KafkaProducer;
import com.h.udemy.java.uservices.order.message.mapper.OrderMessagingDataMapper;
import com.h.udemy.java.uservices.order.service.domain.config.OrderServiceConfigData;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements DomainEventPublisher<OrderPaidEvent> {

    private static final String AVRO_MODEL_NAME= "RestaurantApprovalRequestAvroModel";

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public PayOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                         OrderServiceConfigData orderServiceConfigData,
                                         KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
                                         KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderPaidEvent domainEvent) {

        String orderId = null;

        try {
            orderId = domainEvent.getOrder().getId().getValue().toString();
            log.info(ORDER_RECEIVED_ID.build(orderId));

            RestaurantApprovalRequestAvroModel avroModel = orderMessagingDataMapper
                    .orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    orderId,
                    avroModel,
                    kafkaMessageHelper.getKafkaCallback(orderServiceConfigData
                                    .getRestaurantApprovalRequestTopicName(),
                            avroModel,
                            AVRO_MODEL_NAME,
                            orderId)
            );

            log.info(ORDER_SENT_REQUEST_KAFKA.build(avroModel.getOrderId(),
                    orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    avroModel.getCreatedAt()));

        } catch (Exception e) {
            log.error(ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA.build(AVRO_MODEL_NAME,
                    orderId,
                    e.getMessage()));
        }
    }

}
