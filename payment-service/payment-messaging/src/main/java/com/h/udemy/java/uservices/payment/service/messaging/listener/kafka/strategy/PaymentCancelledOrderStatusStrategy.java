package com.h.udemy.java.uservices.payment.service.messaging.listener.kafka.strategy;

import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.PAYMENT_REQUEST_CANCELLING_FOR_ID;

@Slf4j
@Component
public class PaymentCancelledOrderStatusStrategy implements PaymentOrderStatusStrategy {
    @Override
    public void processPayment(PaymentRequestMessageListener paymentRequestListener,
                               PaymentRequest paymentRequest) {
        log.info(PAYMENT_REQUEST_CANCELLING_FOR_ID.build(paymentRequest.getOrderId()));

        paymentRequestListener.cancelPayment(paymentRequest);
    }
}
