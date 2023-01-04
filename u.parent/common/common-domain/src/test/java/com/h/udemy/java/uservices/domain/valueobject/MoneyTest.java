package com.h.udemy.java.uservices.domain.valueobject;

import com.h.udemy.java.uservices.domain.ApiEnvTestConfig;
import com.h.udemy.java.uservices.domain.util.numbers.Numbers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootConfiguration
class MoneyTest extends ApiEnvTestConfig {

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAmount() {
        Money money1 = new Money(new BigDecimal(2.02));
        BigDecimal amount = money1.getAmount();

        assertEquals(money1, new Money(amount));
    }

    @Test
    void isGreaterThanZero() {
        Money money1 = new Money(new BigDecimal(0.01));

        assertTrue(money1.isGreaterThanZero());
    }

    @Test
    void compareTo_ok() {
        final Money money1 = new Money(new BigDecimal(0.01));
        final Money money2 = new Money(new BigDecimal(0.01));

        assertTrue(money1.compareTo(money2.getAmount()));
    }

    @Test
    void compareTo_ko() {
        final Money money1 = new Money(new BigDecimal(0.01));
        final Money money2 = new Money(new BigDecimal(0.02));

        assertTrue(!money1.compareTo(money2.getAmount()));
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

        assertEquals(money1, new Money(new BigDecimal(2.04)));
    }

    @Test
    void substract() {
        Money money1 = new Money(new BigDecimal(2.02));
        Money money2 = new Money(new BigDecimal(0.02));
        money1 = money1.substract(money2);

        assertEquals(money1, new Money(new BigDecimal(2.00)));
    }

    @Test
    void multiply() {
        Money money1 = new Money(new BigDecimal(2.02));
        money1 = money1.multiply(2);

        assertEquals(money1, new Money(new BigDecimal(4.04)));
    }

    @Test
    void testEquals() {
        Money money1 = new Money(new BigDecimal(1000.999));
        Money money2 = new Money(new BigDecimal(1000.999));

        assertEquals(money1, money2);
    }

    @Test
    void testNotEquals() {
        Money money1 = new Money(new BigDecimal(1000.999));
        Money money2 = new Money(new BigDecimal(1000.988));

        assertFalse(money1.equals(money2));

    }

    @Test
    void testHashCode() {
        Money money = new Money(new BigDecimal(333));
        assertTrue(Numbers.isInteger(money.hashCode()));
    }

    @Test
    void testException() {
        assertFalse(Numbers.isInteger(null));
    }

}