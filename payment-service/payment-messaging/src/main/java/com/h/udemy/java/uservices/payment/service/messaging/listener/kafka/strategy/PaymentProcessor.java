package com.h.udemy.java.uservices.payment.service.messaging.listener.kafka.strategy;

import com.h.udemy.java.uservices.domain.valueobject.PaymentOrderStatus;
import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentRequestAvroModel;
import com.h.udemy.java.uservices.payment.domain.service.PaymentRequestMessageListener;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.PAYMENT_ERR_STATUS_UNSUPPORTED;

@Slf4j
@Component
public class PaymentProcessor {
    private final Map<PaymentOrderStatus, IPaymentOrderStatusStrategy> strategies = new HashMap<>();
    private final PaymentRequestMessageListener paymentRequestListener;
    private final PaymentMessagingDataMapper mapper;

    public PaymentProcessor(PaymentRequestMessageListener paymentRequestListener,
                            PaymentMessagingDataMapper mapper) {
        this.paymentRequestListener = paymentRequestListener;
        this.mapper = mapper;

        strategies.put(PaymentOrderStatus.PENDING, new PaymentCompletedOrderStatusStrategy());
        strategies.put(PaymentOrderStatus.CANCELLED, new PaymentCancelledOrderStatusStrategy());
    }

    public void processPayment(PaymentRequestAvroModel paymentRequestAvroModel) {
        PaymentRequest paymentRequest = mapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel);
        PaymentOrderStatus paymentOrderStatus = paymentRequest.getPaymentOrderStatus();
        IPaymentOrderStatusStrategy paymentOrderStatusStrategy = strategies.get(paymentOrderStatus);
        if (paymentOrderStatusStrategy != null) {
            paymentOrderStatusStrategy.processPayment(paymentRequestListener, paymentRequest);
        } else {
            log.error(PAYMENT_ERR_STATUS_UNSUPPORTED.build(paymentOrderStatus));
        }
    }
}
