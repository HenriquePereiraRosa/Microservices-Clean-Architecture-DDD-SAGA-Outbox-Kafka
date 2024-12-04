package com.h.udemy.java.uservices.payment.domain.service.ports.input.message.listener;

import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;

public interface IPaymentRequestMessageListener {
    void completePayment(PaymentRequest paymentRequest);
    void cancelPayment(PaymentRequest paymentRequest);
}