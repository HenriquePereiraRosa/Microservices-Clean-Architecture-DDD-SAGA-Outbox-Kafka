package com.h.udemy.java.uservices.payment.service.messaging.listener.kafka.strategy;

import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentOrderStatus;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.KAFKA_PROCESSING_FOR_ID;

@Slf4j
@Component
public class PaymentCompletedOrderStatusStrategy implements PaymentOrderStatusStrategy {

    @Override
    public void processPayment(PaymentRequestMessageListener paymentRequestListener,
                               PaymentRequest paymentRequest) {
        if (PaymentOrderStatus.PENDING == PaymentOrderStatus.valueOf(paymentRequest.getPaymentOrderStatus().name())) {
            log.info(KAFKA_PROCESSING_FOR_ID.build(
                    PaymentRequest.class.getSimpleName(),
                    paymentRequest.getOrderId()));

            paymentRequestListener.completePayment(paymentRequest);
        }
    }
}
