package com.h.udemy.java.uservices.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    private final BigDecimal amount;

    public Money(BigDecimal pAmount) {
        this.amount = setScale(pAmount);
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public boolean isGreaterThanZero() {
        return this.amount != null
                && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money pMoney) {
        return this.amount != null
                && this.amount.compareTo(pMoney.getAmount()) > 0;
    }

    public Money add(Money pMoney) {
        return new Money(setScale(
                this.amount.add(pMoney.getAmount())
        ));
    }

    public Money substract(Money pMoney) {
        return new Money(setScale(
                this.amount.subtract(pMoney.getAmount())
        ));
    }

    public Money multiply(int pMultiplier) {
        return new Money(setScale(
                this.amount.multiply(new BigDecimal(pMultiplier))
        ));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return this.amount.compareTo(money.getAmount()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    private BigDecimal setScale(BigDecimal pInput) {
        return pInput.setScale(2, RoundingMode.HALF_EVEN);
    }
}
