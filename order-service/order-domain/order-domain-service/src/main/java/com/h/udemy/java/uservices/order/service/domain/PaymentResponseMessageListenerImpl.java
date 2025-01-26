package com.h.udemy.java.uservices.order.service.domain;

import static com.h.udemy.java.uservices.domain.messages.Messages.ORDER_ROLLBACK_DONE_MSGS;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.PROCESS_OPERATION_COMPLETED;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.h.udemy.java.uservices.order.service.domain.dto.message.PaymentResponse;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.h.udemy.java.uservices.order.service.domain.saga.OrderPaymentSaga;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;

    public PaymentResponseMessageListenerImpl(OrderPaymentSaga orderPaymentSaga) {
        this.orderPaymentSaga = orderPaymentSaga;
    }


    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        orderPaymentSaga.process(paymentResponse);
        log.info(PROCESS_OPERATION_COMPLETED.build(
                "OrderPaidEvent",
                paymentResponse.getOrderId()));
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info(ORDER_ROLLBACK_DONE_MSGS
                .build("OrderPaidEvent", paymentResponse.getOrderId()));
    }
}
