package com.h.udemy.java.uservices.payment.domain.core;

import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.CreditEntryId;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.TransacionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.Const.ZONED_UTC;
import static com.h.udemy.java.uservices.domain.messages.Msgs.ERR_PAYMENT_NOT_ENOUGH_CREDIT;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;

@Slf4j
public class PaymentDomainService implements IPaymentDomainService {

    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistories,
                                                   List<String> failureMessages) {

        failureMessages.add(payment.validatePaymentReturningFailuresMsgs());
        payment.initializePayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        creditEntry.subtractCreditAmount(payment.getPrice());
        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if(CollectionUtils.isEmpty(failureMessages)) {
            log.info(PAYMENT_REQUEST_SUCCESS_FOR_ID.get(), payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);

            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZONED_UTC));
        }

        log.info(PAYMENT_ERR_FAILED_FOR_ORDER_ID.get(), payment.getOrderId().getValue());
        payment.updateStatus(PaymentStatus.FAILED);

        return new PaymentFailedEvent(payment, ZonedDateTime.now(ZONED_UTC), failureMessages);
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistories,
                                                 List<String> failureMessages) {

        failureMessages.add(payment.validatePaymentReturningFailuresMsgs());
        creditEntry.addCreditAmount(payment.getPrice());
        updateCreditHistory(payment, creditHistories, TransacionType.CREDIT);

        if(CollectionUtils.isEmpty(failureMessages)) {
            log.info(PAYMENT_REQUEST_CANCELED_FOR_ID.get(), payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);

            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZONED_UTC));
        }

        log.info(PAYMENT_ERR_FAILED_FOR_ORDER_ID.get(), payment.getOrderId().getValue());
        payment.updateStatus(PaymentStatus.FAILED);

        return new PaymentFailedEvent(payment, ZonedDateTime.now(ZONED_UTC), failureMessages);
    }


    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getToralCreditAmount())) {
            log.error(PAYMENT_ERR_NOT_ENOUGH_CREDIT.get(), payment.getCustomerId().getValue());

            failureMessages.add(ERR_PAYMENT_NOT_ENOUGH_CREDIT.get() + payment.getCustomerId());
        }
    }

    private void updateCreditHistory(Payment payment,
                                     List<CreditHistory> creditHistories,
                                     TransacionType transacionType) {
        creditHistories.add(CreditHistory.Builder.builder()
                .creditEntryId(new CreditEntryId(UUID.randomUUID()))
                .customerId(payment.getCustomerId())
                .amount(payment.getPrice())
                .transacionType(transacionType)
                .build());
    }

    private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransacionType.CREDIT);

        Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransacionType.DEBIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            log.error(PAYMENT_ERR_NOT_ENOUGH_CREDIT.get(), creditEntry.getCustomerId().getValue());

            failureMessages.add(ERR_PAYMENT_NOT_ENOUGH_CREDIT.get() + creditEntry.getCustomerId());
        }

        Money danglingDebit = totalCreditHistory.substract(totalDebitHistory);
        if (!creditEntry.getToralCreditAmount().equals(danglingDebit)) {
            log.error(PAYMENT_ERR_CREDIT_HISTORY_NOT_EQUALS.get(), creditEntry.getCustomerId().getValue());

            failureMessages.add(ERR_PAYMENT_NOT_ENOUGH_CREDIT.get() + creditEntry.getCustomerId());
        }
    }

    private Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransacionType transacionType) {
        return creditHistories.stream()
                .filter(creditHistory -> transacionType == creditHistory.getTransacionType())
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }
}
