package com.h.udemy.java.uservices.publisher.kafka;

import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentRequestAvroModel;
import com.h.udemy.java.uservices.kafka.producer.KafkaMessageHelper;
import com.h.udemy.java.uservices.kafka.producer.service.impl.KafkaProducer;
import com.h.udemy.java.uservices.mapper.OrderMessagingDataMapper;
import com.h.udemy.java.uservices.order.service.domain.config.OrderServiceConfigData;
import com.h.udemy.java.uservices.order.service.domain.event.OrderCancelledEvent;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.IOrderCancelledPaymentRequestRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_CANCEL_RECEIVED_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_SENT_REQUEST_KAFKA;

@Slf4j
@Component
public class CancelOrderKafkaMessagePublisher implements IOrderCancelledPaymentRequestRequestMessagePublisher {

    private static final String AVRO_MODEL_NAME= "PaymentRequestAvroModel";

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public CancelOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                            OrderServiceConfigData orderServiceConfigData,
                                            KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer,
                                            KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderCancelledEvent domainEvent) {

        String orderId = null;

        try {
            orderId = domainEvent.getOrder().getId().getValue().toString();
            log.info(ORDER_CANCEL_RECEIVED_ID.build(orderId));

            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper
                    .orderCancelledEventToPaymentRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(orderServiceConfigData
                                    .getPaymentResponseTopicName(),
                            paymentRequestAvroModel,
                            AVRO_MODEL_NAME,
                            orderId)
            );

            log.info(ORDER_SENT_REQUEST_KAFKA.build(paymentRequestAvroModel.getOrderId()));

        } catch (Exception e) {
            log.error(ORDER_ERROR_MSG_SENDING_REQ_AVRO_KAFKA.build(
                    AVRO_MODEL_NAME,
                    orderId,
                    e.getMessage()));
        }
    }
}
