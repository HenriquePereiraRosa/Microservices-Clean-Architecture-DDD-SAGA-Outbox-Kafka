package com.h.udemy.java.uservices.payment.service.messaging.publisher.kafka;

import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentResponseAvroModel;
import com.h.udemy.java.uservices.kafka.producer.KafkaMessageHelper;
import com.h.udemy.java.uservices.kafka.producer.service.impl.KafkaProducer;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.service.config.PaymentServiceConfigData;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.IPaymentCompletedMessagePublisher;
import com.h.udemy.java.uservices.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.EVENT_ERR_SENT_TO_KAFKA;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.EVENT_RECEIVED;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.EVENT_SENT_TO_KAFKA;

@Slf4j
@Component
public class PaymentCompletedKafkaMessagePublisher implements IPaymentCompletedMessagePublisher {

    public static final String AVRO_MODEL_NAME = "PaymentResponseAvroModel";
    public static final String TARGET_NAME = "OrderId";
    public static final String EVENT_NAME = "PaymentCompletedEvent";

    private final PaymentMessagingDataMapper mapper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;


    public PaymentCompletedKafkaMessagePublisher(PaymentMessagingDataMapper mapper,
                                                 KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
                                                 PaymentServiceConfigData paymentServiceConfigData,
                                                 KafkaMessageHelper kafkaMessageHelper) {
        this.mapper = mapper;
        this.kafkaProducer = kafkaProducer;
        this.paymentServiceConfigData = paymentServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(PaymentCompletedEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderId().getValue().toString();

        log.info(EVENT_RECEIVED.build(EVENT_NAME, TARGET_NAME, orderId));

        try {
            PaymentResponseAvroModel avroModel = mapper.paymentCompletedEventToPaymentResponseAvroModel(domainEvent);

            kafkaProducer.send(paymentServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    avroModel,
                    kafkaMessageHelper.getKafkaCallback(paymentServiceConfigData.getPaymentRequestTopicName(),
                            avroModel,
                            orderId,
                            AVRO_MODEL_NAME));

            log.info(EVENT_SENT_TO_KAFKA.build(AVRO_MODEL_NAME, TARGET_NAME, orderId));
        } catch (Exception e) {
            log.info(EVENT_ERR_SENT_TO_KAFKA.build(AVRO_MODEL_NAME, TARGET_NAME, orderId, e.getMessage()));
        }
    }
}
