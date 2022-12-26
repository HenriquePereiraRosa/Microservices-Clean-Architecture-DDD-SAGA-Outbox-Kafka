package com.h.udemy.java.uservices.publisher.kafka;

import com.h.udemy.java.uservices.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.h.udemy.java.uservices.kafka.producer.service.impl.KafkaProducer;
import com.h.udemy.java.uservices.mapper.OrderMessagingDataMapper;
import com.h.udemy.java.uservices.order.service.domain.config.OrderServiceConfigData;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.IOrderPaidRestaurantRequestRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_RECEIVED_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_SENT_REQUEST_KAFKA;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements IOrderPaidRestaurantRequestRequestMessagePublisher {

    private final String AVRO_MODEL_NAME= "RestaurantApprovalRequestAvroModel";
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderKafkaMessageHelper orderKafkaMessageHelper;

    public PayOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                         OrderServiceConfigData orderServiceConfigData,
                                         KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
                                         OrderKafkaMessageHelper orderKafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.orderKafkaMessageHelper = orderKafkaMessageHelper;
    }

    @Override
    public void publish(OrderPaidEvent domainEvent) {

        String orderId = null;

        try {
            orderId = domainEvent.getOrder().getId().getValue().toString();
            log.info(ORDER_RECEIVED_ID.get(), orderId);

            RestaurantApprovalRequestAvroModel avroModel = orderMessagingDataMapper
                    .orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    avroModel,
                    orderKafkaMessageHelper.getKafkaCallback(orderServiceConfigData
                                    .getPaymentResponseTopicName(),
                            avroModel,
                            AVRO_MODEL_NAME,
                            orderId)
            );

            log.info(ORDER_SENT_REQUEST_KAFKA.get(),
                    avroModel.getOrderId());

        } catch (Exception e) {
            log.error(ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA.get(),
                    AVRO_MODEL_NAME,
                    orderId,
                    e.getMessage());
        }
    }

}
