package com.h.udemy.java.uservices.payment.domain.core.entity;

import com.h.udemy.java.uservices.domain.entity.AggregateRoot;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.PaymentId;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.Constants.ZONED_UTC;
import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_PAYMENT_TOTAL_PRICE_MUST_BE_GRATER_THAN_ZERO;

@Getter
public class Payment extends AggregateRoot<PaymentId> {

    private final OrderId orderId;
    private final CustomerId customerId;
    private final Money price;
    private PaymentStatus paymentStatus;
    private ZonedDateTime createdAt;

    private Payment(Builder builder) {
        setId(builder.paymentId);
        orderId = builder.orderId;
        customerId = builder.customerId;
        price = builder.price;
        paymentStatus = builder.paymentStatus;
        createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    // InnerBuilder
    public static final class Builder {
        private PaymentId paymentId;
        private OrderId orderId;
        private CustomerId customerId;
        private Money price;
        private PaymentStatus paymentStatus;
        private ZonedDateTime createdAt;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder paymentId(PaymentId val) {
            paymentId = val;
            return this;
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder paymentStatus(PaymentStatus val) {
            paymentStatus = val;
            return this;
        }

        public Builder createdAt(ZonedDateTime val) {
            createdAt = val;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }


    public void initializePayment() {
        setId(new PaymentId(UUID.randomUUID()));
        createdAt = ZonedDateTime.now(ZONED_UTC);
    }

    public String validatePaymentReturningFailuresMsgs() {
        if(price == null || !price.isGreaterThanZero()) {
            return ERR_PAYMENT_TOTAL_PRICE_MUST_BE_GRATER_THAN_ZERO.get();
        }

        return null;
    }

    public void updateStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
