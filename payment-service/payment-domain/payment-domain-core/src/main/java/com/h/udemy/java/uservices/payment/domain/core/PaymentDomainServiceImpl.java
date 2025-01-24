package com.h.udemy.java.uservices.payment.domain.core;

import com.h.udemy.java.uservices.domain.event.DomainEventPublisher;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.PaymentStatus;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditEntry;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.entity.Payment;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCancelledEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentCompletedEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentEvent;
import com.h.udemy.java.uservices.payment.domain.core.event.PaymentFailedEvent;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.CreditHistoryId;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.*;

import static com.h.udemy.java.uservices.domain.Constants.ZONED_UTC;
import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_PAYMENT_NOT_ENOUGH_CREDIT;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Component
public class PaymentDomainServiceImpl implements PaymentDomainService {

    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistories,
                                                   List<String> failureMessages,
                                                   DomainEventPublisher<PaymentCompletedEvent> completedEventPublisher,
                                                   DomainEventPublisher<PaymentFailedEvent> failedEventPublisher) {
        failureMessages = validateAndAddMessages(payment);
        payment.initializePayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (CollectionUtils.isEmpty(failureMessages)) {
            log.info(PAYMENT_REQUEST_SUCCESS_FOR_ID.build(payment.getOrderId().getValue()));
            payment.updateStatus(PaymentStatus.COMPLETED);

            return new PaymentCompletedEvent(payment,
                    ZonedDateTime.now(ZONED_UTC),
                    completedEventPublisher);
        }

        log.info(PAYMENT_ERR_FAILED_FOR_ORDER_ID.build(payment.getOrderId().getValue()));
        payment.updateStatus(PaymentStatus.FAILED);

        return new PaymentFailedEvent(payment,
                ZonedDateTime.now(ZONED_UTC),
                failureMessages,
                failedEventPublisher);
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistories,
                                                 List<String> failureMessages,
                                                 DomainEventPublisher<PaymentCancelledEvent> cancelledEventPublisher,
                                                 DomainEventPublisher<PaymentFailedEvent> failedEventPublisher) {

        failureMessages = validateAndAddMessages(payment);
        creditEntry.addCreditAmount(payment.getPrice());
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);

        if (CollectionUtils.isEmpty(failureMessages)) {
            log.info(PAYMENT_REQUEST_CANCELED_FOR_ID.build(payment.getOrderId().getValue()));
            payment.updateStatus(PaymentStatus.CANCELLED);

            return new PaymentCancelledEvent(payment,
                    ZonedDateTime.now(ZONED_UTC),
                    cancelledEventPublisher);
        }

        log.info(PAYMENT_ERR_FAILED_FOR_ORDER_ID.build(payment.getOrderId().getValue()));
        payment.updateStatus(PaymentStatus.FAILED);

        return new PaymentFailedEvent(payment,
                ZonedDateTime.now(ZONED_UTC),
                failureMessages,
                failedEventPublisher);
    }

    private static List<String> validateAndAddMessages(Payment payment) {
        String failureMsg = payment.validatePaymentReturningFailuresMsgs();
        if (isBlank(failureMsg)) {
            return new ArrayList<>();
        }

        return Collections.singletonList(payment.validatePaymentReturningFailuresMsgs());
    }


    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            log.error(PAYMENT_ERR_NOT_ENOUGH_CREDIT.build(payment.getCustomerId().getValue()));

            failureMessages.add(ERR_PAYMENT_NOT_ENOUGH_CREDIT.get() + payment.getCustomerId());
        }
    }


    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment,
                                     List<CreditHistory> creditHistories,
                                     TransactionType transactionType) {
        creditHistories.add(CreditHistory.Builder.builder()
                .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                .customerId(payment.getCustomerId())
                .amount(payment.getPrice())
                .transactionType(transactionType)
                .build());
    }

    private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);

        Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            log.error(PAYMENT_ERR_NOT_ENOUGH_CREDIT.build(), creditEntry.getCustomerId().getValue());

            failureMessages.add(ERR_PAYMENT_NOT_ENOUGH_CREDIT.build(creditEntry.getCustomerId()));
        }

        Money remanescentCredit = totalCreditHistory.subtract(totalDebitHistory);
        if (isCreditHistoryAndTotalCreditAmountNotEqual(creditEntry, remanescentCredit)) {
            log.error(PAYMENT_ERR_CREDIT_HISTORY_NOT_EQUALS.build(), creditEntry.getCustomerId().getValue());

            failureMessages.add(ERR_PAYMENT_NOT_ENOUGH_CREDIT.get() + creditEntry.getCustomerId());
        }
    }

    private static boolean isCreditHistoryAndTotalCreditAmountNotEqual(CreditEntry creditEntry, Money remanescentCredit) {
        return !remanescentCredit.isEqual(creditEntry.getTotalCreditAmount().getAmount());
    }

    private Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
        return creditHistories.stream()
                .filter(creditHistory -> transactionType == creditHistory.getTransactionType())
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }
}
