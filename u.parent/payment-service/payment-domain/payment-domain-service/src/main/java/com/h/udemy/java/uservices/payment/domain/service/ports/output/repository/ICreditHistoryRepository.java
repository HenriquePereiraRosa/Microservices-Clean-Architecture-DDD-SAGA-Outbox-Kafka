package com.h.udemy.java.uservices.payment.domain.service.ports.output.repository;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;

import java.util.List;
import java.util.Optional;

public interface ICreditHistoryRepository {
    CreditHistory save(CreditHistory creditHistory);
    Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId);
}