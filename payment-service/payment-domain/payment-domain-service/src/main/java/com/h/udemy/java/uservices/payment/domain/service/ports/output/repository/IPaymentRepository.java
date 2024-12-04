package com.h.udemy.java.uservices.payment.domain.service.ports.output.repository;

import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;

import java.util.Optional;
import java.util.UUID;

public interface IPaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByOrderId(UUID orderId);
}