package com.h.udemy.java.uservices.payment.service.messaging.listener.kafka.strategy;

import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentOrderStatus;
import com.h.udemy.java.uservices.payment.domain.service.PaymentRequestMessageListener;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.KAFKA_PROCESSING_FOR_ID;

@Slf4j
@Component
public class PaymentCompletedOrderStatusStrategy implements IPaymentOrderStatusStrategy {

    private static final String REQUEST_SERVICE_NAME = "Payment Request";
    @Override
    public void processPayment(PaymentRequestMessageListener paymentRequestListener,
                               PaymentRequest paymentRequest) {
        if (PaymentOrderStatus.PENDING.equals(paymentRequest.getPaymentOrderStatus())) {
            log.info(KAFKA_PROCESSING_FOR_ID.build(paymentRequest.getOrderId()));

            paymentRequestListener.completePayment(paymentRequest);
        }
    }
}
