package com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.payment;

import com.h.udemy.java.uservices.order.service.domain.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}
