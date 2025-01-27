package com.h.udemy.java.uservices.saga.strategy;

import com.h.udemy.java.uservices.domain.valueobject.OrderStatus;
import com.h.udemy.java.uservices.saga.SagaStatus;

import java.util.HashMap;
import java.util.Map;

public class SagaStatusStrategyContext {
private static final Map<OrderStatus, SagaStatusStrategy> strategies = new HashMap<>();

static {
    strategies.put(OrderStatus.PAID, new PaidSagaStatusStrategy());
    strategies.put(OrderStatus.APPROVED, new ApprovedSagaStatusStrategy());
    strategies.put(OrderStatus.CANCELLING, new CancellingSagaStatusStrategy());
    strategies.put(OrderStatus.CANCELLED, new CancelledSagaStatusStrategy());
}

public static SagaStatus getSagaStatusFromOrderStatus(OrderStatus orderStatus) {
    return strategies.getOrDefault(orderStatus, new DefaultSagaStatusStrategy()).getSagaStatus();
}
}
