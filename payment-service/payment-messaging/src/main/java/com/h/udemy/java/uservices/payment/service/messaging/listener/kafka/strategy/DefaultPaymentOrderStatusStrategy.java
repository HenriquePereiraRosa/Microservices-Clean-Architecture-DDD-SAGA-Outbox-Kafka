package com.h.udemy.java.uservices.payment.service.messaging.listener.kafka.strategy;

import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.PAYMENT_ERR_STATUS_UNSUPPORTED;

@Slf4j
@Component
public class DefaultPaymentOrderStatusStrategy implements PaymentOrderStatusStrategy {

    @Override
    public void processPayment(
            PaymentRequestMessageListener paymentRequestListener,
            PaymentRequest paymentRequest) {
        log.error(PAYMENT_ERR_STATUS_UNSUPPORTED.build(paymentRequest.getPaymentOrderStatus()));
    }
}
