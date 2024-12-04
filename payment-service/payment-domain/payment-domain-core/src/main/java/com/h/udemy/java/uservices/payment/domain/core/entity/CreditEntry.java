package com.h.udemy.java.uservices.payment.domain.core.entity;

import com.h.udemy.java.uservices.domain.entity.BaseEntity;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.CreditEntryId;
import lombok.Getter;

@Getter
public class CreditEntry extends BaseEntity<CreditEntryId> {

    private final CustomerId customerId;
    private Money totalCreditAmount;


    public void addCreditAmount(Money money) {
        totalCreditAmount = totalCreditAmount.add(money);
    }

    public void subtractCreditAmount(Money money) {
        totalCreditAmount = totalCreditAmount.subtract(money);
    }



    private CreditEntry(Builder builder) {
        setId(builder.creditEntryId);
        customerId = builder.customerId;
        totalCreditAmount = builder.toralCreditAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CreditEntryId creditEntryId;
        private CustomerId customerId;
        private Money toralCreditAmount;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder creditEntryId(CreditEntryId val) {
            creditEntryId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            toralCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
