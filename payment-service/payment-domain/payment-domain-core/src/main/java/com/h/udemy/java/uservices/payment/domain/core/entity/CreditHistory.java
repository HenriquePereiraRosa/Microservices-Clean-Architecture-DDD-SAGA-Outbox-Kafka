package com.h.udemy.java.uservices.payment.domain.core.entity;

import com.h.udemy.java.uservices.domain.entity.BaseEntity;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.CreditHistoryId;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.TransactionType;
import lombok.Getter;

@Getter
public class CreditHistory extends BaseEntity<CreditHistoryId> {

    private final CustomerId customerId;
    private final Money amount;
    private final TransactionType transactionType;

    private CreditHistory(Builder builder) {
        setId(builder.creditHistoryId);
        this.customerId = builder.customerId;
        this.amount = builder.amount;
        this.transactionType = builder.transactionType;
    }


    public static Builder builder() {
        return new Builder();
    }

    // InnerBuilder
    public static final class Builder {
        private CreditHistoryId creditHistoryId;
        private CustomerId customerId;
        private Money amount;
        private TransactionType transactionType;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder creditHistoryId(CreditHistoryId val) {
            creditHistoryId = val;
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

        public Builder transactionType(TransactionType val) {
            transactionType = val;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(this);
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
