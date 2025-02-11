package com.h.udemy.java.uservices.payment.service.messaging.listener.kafka.strategy;

import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.ports.input.message.listener.PaymentRequestMessageListener;
import org.springframework.stereotype.Component;

@Component
public interface PaymentOrderStatusStrategy {
    void processPayment(PaymentRequestMessageListener paymentRequestListener,
                        PaymentRequest paymentRequest);
}
