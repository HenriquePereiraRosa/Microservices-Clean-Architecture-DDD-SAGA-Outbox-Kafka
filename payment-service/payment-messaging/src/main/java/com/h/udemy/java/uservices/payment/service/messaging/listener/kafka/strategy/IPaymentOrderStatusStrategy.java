package com.h.udemy.java.uservices.payment.service.messaging.listener.kafka.strategy;

import com.h.udemy.java.uservices.payment.domain.service.PaymentRequestMessageListener;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public interface IPaymentOrderStatusStrategy {
    void processPayment(PaymentRequestMessageListener paymentRequestListener,
                        PaymentRequest paymentRequest);
}
