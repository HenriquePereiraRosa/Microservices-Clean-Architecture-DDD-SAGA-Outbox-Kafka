package com.h.udemy.java.uservices.payment.service.dataaccess.creditentry.adapter;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.service.dataaccess.creditentry.mapper.CreditEntryDataAccessMapper;
import com.h.udemy.java.uservices.payment.service.dataaccess.creditentry.repository.CreditEntryJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreditEntryRepository implements com.h.udemy.java.uservices.payment.domain.service.ports.output.repository.CreditEntryRepository {

    private final CreditEntryJpaRepository creditEntryJpaRepository;
    private final CreditEntryDataAccessMapper creditEntryDataAccessMapper;

    public CreditEntryRepository(CreditEntryJpaRepository creditEntryJpaRepository,
                                 CreditEntryDataAccessMapper creditEntryDataAccessMapper) {
        this.creditEntryJpaRepository = creditEntryJpaRepository;
        this.creditEntryDataAccessMapper = creditEntryDataAccessMapper;
    }

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        return creditEntryDataAccessMapper
                .creditEntryEntityToCreditEntry(creditEntryJpaRepository
                        .save(creditEntryDataAccessMapper.creditEntryToCreditEntryEntity(creditEntry)));
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(CustomerId customerId) {
        return creditEntryJpaRepository
                .findByCustomerId(customerId.getValue())
                .map(creditEntryDataAccessMapper::creditEntryEntityToCreditEntry);
    }
}
