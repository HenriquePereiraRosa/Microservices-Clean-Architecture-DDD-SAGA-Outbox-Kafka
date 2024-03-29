package com.h.udemy.java.uservices.payment.domain.service;

import com.h.udemy.java.uservices.payment.domain.core.event.PaymentEvent;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.ports.input.message.listener.IPaymentRequestMessageListener;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.IPaymentCancelledMessagePublisher;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.IPaymentCompletedMessagePublisher;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.message.publisher.IPaymentFailedMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.h.udemy.java.uservices.domain.messages.Messages.PAYMENT_PUB_EVENT_PAYMENT_AND_ORDER_FOR_ID;
import static java.text.MessageFormat.format;

@Slf4j
@Service
public class PaymentRequestMessageListener implements IPaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;
    private final IPaymentCompletedMessagePublisher iPaymentCompletedMessagePublisher;
    private final IPaymentCancelledMessagePublisher iPaymentCancelledMessagePublisher;
    private final IPaymentFailedMessagePublisher iPaymentFailedMessagePublisher;

    public PaymentRequestMessageListener(PaymentRequestHelper paymentRequestHelper,
                                         IPaymentCompletedMessagePublisher iPaymentCompletedMessagePublisher,
                                         IPaymentCancelledMessagePublisher iPaymentCancelledMessagePublisher,
                                         IPaymentFailedMessagePublisher iPaymentFailedMessagePublisher) {
        this.paymentRequestHelper = paymentRequestHelper;
        this.iPaymentCompletedMessagePublisher = iPaymentCompletedMessagePublisher;
        this.iPaymentCancelledMessagePublisher = iPaymentCancelledMessagePublisher;
        this.iPaymentFailedMessagePublisher = iPaymentFailedMessagePublisher;
    }

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistCancelPayment(paymentRequest);
        fireEvent(paymentEvent);

    }

    private void fireEvent(PaymentEvent paymentEvent) {
        log.info(format(PAYMENT_PUB_EVENT_PAYMENT_AND_ORDER_FOR_ID.get(),
                paymentEvent.getPayment().getId().getValue(),
                paymentEvent.getPayment().getOrderId().getValue()));

        paymentEvent.fire();
    }
}