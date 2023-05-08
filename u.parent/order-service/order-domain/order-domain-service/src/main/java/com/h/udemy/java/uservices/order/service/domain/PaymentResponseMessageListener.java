package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.message.PaymentResponse;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.payment.IPaymentResponseMessageListener;
import com.h.udemy.java.uservices.order.service.domain.saga.OrderPaymentSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.h.udemy.java.uservices.domain.messages.Messages.ORDER_ROLLBACK_DONE_MSGS;
import static com.h.udemy.java.uservices.domain.messages.Messages.PUBLISHING_EVENT_FOR_ID;


@Slf4j
@Validated
@Service
public class PaymentResponseMessageListener implements IPaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;

    public PaymentResponseMessageListener(OrderPaymentSaga orderPaymentSaga) {
        this.orderPaymentSaga = orderPaymentSaga;
    }


    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        OrderPaidEvent domainEvent = orderPaymentSaga.process(paymentResponse);
        log.info(PUBLISHING_EVENT_FOR_ID
                .build("OrderPaidEvent", paymentResponse.getOrderId()));
        domainEvent.fire();
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info(ORDER_ROLLBACK_DONE_MSGS
                .build("OrderPaidEvent", paymentResponse.getOrderId()));
    }
}
