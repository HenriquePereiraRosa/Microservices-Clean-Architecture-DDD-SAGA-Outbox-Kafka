package com.h.udemy.java.uservices.domain.valueobject;

import com.h.udemy.java.uservices.domain.util.numbers.Numbers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAmount() {
        Money money1 = new Money(new BigDecimal(2.02));
        BigDecimal amount = money1.getAmount();

        assertTrue(money1.equals(new Money(amount)));
    }

    @Test
    void isGreaterThanZero() {
        Money money1 = new Money(new BigDecimal(0.01));

        assertTrue(money1.isGreaterThanZero());
    }

    @Test
    void isGreaterThan() {
        Money money1 = new Money(new BigDecimal(2.02));
        Money money2 = new Money(new BigDecimal(0.02));

        assertTrue(money1.isGreaterThan(money2));
    }

    @Test
    void add() {
        Money money1 = new Money(new BigDecimal(2.02));
        Money money2 = new Money(new BigDecimal(0.02));
        money1 = money1.add(money2);

        assertTrue(money1.equals(new Money(new BigDecimal(2.04))));
    }

    @Test
    void substract() {
        Money money1 = new Money(new BigDecimal(2.02));
        Money money2 = new Money(new BigDecimal(0.02));
        money1 = money1.substract(money2);

        assertTrue(money1.equals(new Money(new BigDecimal(2.00))));
    }

    @Test
    void multiply() {
        Money money1 = new Money(new BigDecimal(2.02));
        money1 = money1.multiply(2);

        assertTrue(money1.equals(new Money(new BigDecimal(4.04))));
    }

    @Test
    void testEquals() {
        Money money1 = new Money(new BigDecimal(1000.999));
        Money money2 = new Money(new BigDecimal(1000.999));

        assertTrue(money1.equals(money2));
    }

    @Test
    void testNotEquals() {
        Money money1 = new Money(new BigDecimal(1000.999));
        Money money2 = new Money(new BigDecimal(1000.988));

        assertTrue(!money1.equals(money2));

    }

    @Test
    void testHashCode() {
        Money money = new Money(new BigDecimal(333));
        assertTrue(Numbers.isInteger(money.hashCode()));
    }

}