package com.h.udemy.java.uservices.payment.domain.core.entity;

import com.h.udemy.java.uservices.domain.entity.BaseEntity;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.CreditEntryId;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.TransacionType;
import com.h.udemy.java.uservices.valueobject.TransacionType;
import com.h.udemy.java.uservices.valueobject.CreditEntryId;

public class CreditHistory extends BaseEntity<CreditEntryId> {

    private final CustomerId customerId;
    private final Money amount;
    private final TransacionType transacionType;

    private CreditHistory(Builder builder) {
        setId(builder.creditEntryId);
        customerId = builder.customerId;
        amount = builder.amount;
        transacionType = builder.transacionType;
    }


    public static final class Builder {
        private CreditEntryId creditEntryId;
        private CustomerId customerId;
        private Money amount;
        private TransacionType transacionType;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(CreditEntryId val) {
            creditEntryId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder amount(Money val) {
            amount = val;
            return this;
        }

        public Builder transacionType(TransacionType val) {
            transacionType = val;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(this);
        }
    }
}
