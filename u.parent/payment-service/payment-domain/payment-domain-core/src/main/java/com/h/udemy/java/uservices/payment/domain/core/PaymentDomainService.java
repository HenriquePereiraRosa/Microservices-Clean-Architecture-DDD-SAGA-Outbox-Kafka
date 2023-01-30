package com.h.udemy.java.uservices.payment.domain.core;

import com.h.udemy.java.uservices.domain.event.IDomainEventPublisher;
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
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.Constants.ZONED_UTC;
import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_PAYMENT_NOT_ENOUGH_CREDIT;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.*;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Component
public class PaymentDomainService implements IPaymentDomainService {

    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistories,
                                                   List<String> failureMessages,
                                                   IDomainEventPublisher<PaymentCompletedEvent> completedEventPublisher,
                                                   IDomainEventPublisher<PaymentFailedEvent> failedEventPublisher) {
        failureMessages = validateAndAddMessages(payment);
        payment.initializePayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        creditEntry.subtractCreditAmount(payment.getPrice());
        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (CollectionUtils.isEmpty(failureMessages)) {
            log.info(PAYMENT_REQUEST_SUCCESS_FOR_ID.get(), payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);

            return new PaymentCompletedEvent(payment,
                    ZonedDateTime.now(ZONED_UTC),
                    completedEventPublisher);
        }

        log.info(PAYMENT_ERR_FAILED_FOR_ORDER_ID.get(), payment.getOrderId().getValue());
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
                                                 IDomainEventPublisher<PaymentCancelledEvent> cancelledEventPublisher,
                                                 IDomainEventPublisher<PaymentFailedEvent> failedEventPublisher) {

        failureMessages = validateAndAddMessages(payment);
        creditEntry.addCreditAmount(payment.getPrice());
        updateCreditHistory(payment, creditHistories, TransacionType.CREDIT);

        if (CollectionUtils.isEmpty(failureMessages)) {
            log.info(PAYMENT_REQUEST_CANCELED_FOR_ID.get(), payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);

            return new PaymentCancelledEvent(payment,
                    ZonedDateTime.now(ZONED_UTC),
                    cancelledEventPublisher);
        }

        log.info(PAYMENT_ERR_FAILED_FOR_ORDER_ID.get(), payment.getOrderId().getValue());
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

        return Arrays.asList(payment.validatePaymentReturningFailuresMsgs());
    }


    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
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
        if (isCreditNotEntryEnough(creditEntry, danglingDebit)) {
            log.error(PAYMENT_ERR_CREDIT_HISTORY_NOT_EQUALS.get(), creditEntry.getCustomerId().getValue());

            failureMessages.add(ERR_PAYMENT_NOT_ENOUGH_CREDIT.get() + creditEntry.getCustomerId());
        }
    }

    private static boolean isCreditNotEntryEnough(CreditEntry creditEntry, Money danglingDebit) {
        return !creditEntry.getTotalCreditAmount().isGreaterThan(danglingDebit);
    }

    private Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransacionType transacionType) {
        return creditHistories.stream()
                .filter(creditHistory -> transacionType == creditHistory.getTransacionType())
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }
}
