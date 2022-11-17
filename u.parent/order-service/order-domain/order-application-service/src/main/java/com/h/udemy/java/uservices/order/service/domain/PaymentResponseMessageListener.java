package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.message.PaymentResponse;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.payment.IPaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Slf4j
@Validated
@Service
public class PaymentResponseMessageListener implements IPaymentResponseMessageListener {

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {

    }
}
