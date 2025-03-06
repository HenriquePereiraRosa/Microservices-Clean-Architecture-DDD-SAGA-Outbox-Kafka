package com.h.udemy.java.uservices.payment.service.dataaccess.payment.adapter;


import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.PaymentRepository;
import com.h.udemy.java.uservices.payment.service.dataaccess.payment.mapper.PaymentDataAccessMapper;
import com.h.udemy.java.uservices.payment.service.dataaccess.payment.repository.IPaymentJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentRepositoryI implements PaymentRepository {

    private final IPaymentJpaRepository paymentJpaRepository;
    private final PaymentDataAccessMapper paymentDataAccessMapper;

    public PaymentRepositoryI(IPaymentJpaRepository paymentJpaRepository,
                              PaymentDataAccessMapper paymentDataAccessMapper) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.paymentDataAccessMapper = paymentDataAccessMapper;
    }

    @Override
    public void save(Payment payment) {
        paymentDataAccessMapper
                .paymentEntityToPayment(paymentJpaRepository
                        .save(paymentDataAccessMapper.paymentToPaymentEntity(payment)));
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .map(paymentDataAccessMapper::paymentEntityToPayment);
    }
}
