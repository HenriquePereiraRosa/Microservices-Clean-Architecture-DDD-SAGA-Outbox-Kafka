package com.h.udemy.java.uservices.order.service.domain.saga.strategy;

import java.util.HashMap;
import java.util.Map;

import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.order.service.domain.saga.strategy.impl.CancelledSagaStatusStrategy;
import com.h.udemy.java.uservices.order.service.domain.saga.strategy.impl.CompletedSagaStatusStrategy;
import com.h.udemy.java.uservices.order.service.domain.saga.strategy.impl.FailedSagaStatusStrategy;
import com.h.udemy.java.uservices.saga.SagaStatus;

public class OderPaymentSagaStatusStrategyContext {
private static final Map<PaymentStatus, OrderPaymentSagaStatusStrategy> strategies = new HashMap<>();

static {
    strategies.put(PaymentStatus.COMPLETED, new CompletedSagaStatusStrategy());
    strategies.put(PaymentStatus.CANCELLED, new CancelledSagaStatusStrategy());
    strategies.put(PaymentStatus.FAILED, new FailedSagaStatusStrategy());
}

public static SagaStatus[] getSagaStatusFromPaymentStatus(PaymentStatus paymentStatus) {
    return strategies.get(paymentStatus).getSagaStatus();
}
}
