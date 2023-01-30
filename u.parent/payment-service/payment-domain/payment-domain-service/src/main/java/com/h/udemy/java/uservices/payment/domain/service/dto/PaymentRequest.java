package com.h.udemy.java.uservices.payment.domain.service.dto;

import com.h.udemy.java.uservices.domain.valueobject.PaymentOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class PaymentRequest {
    private String id;
    private String sagaId;
    private String orderId;
    private String customerId;
    private BigDecimal price;
    private PaymentOrderStatus paymentOrderStatus;
    private Instant createdAt;
}