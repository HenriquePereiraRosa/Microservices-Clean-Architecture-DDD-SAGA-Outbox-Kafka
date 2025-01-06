package com.h.udemy.java.uservices.payment.domain.core.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.CreditHistoryId;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.TransactionType;

import java.util.ArrayList;
import java.util.List;

import static com.h.udemy.java.uservices.domain.test.constants.ConstantsTest.CREDIT_HISTORY_UUID;
import static com.h.udemy.java.uservices.domain.test.constants.ConstantsTest.CUSTOMER_UUID;

public class CreditHistoryFactory {

    private static final double CREDIT_VALUE = 1002.22;
    public static final int LIST_SIZE = 10;
    public static final int DEBIT_LIST_SIZE = 3;
    public static final int ODD_DETECTOR = 2;

    static public List<CreditHistory> createOKList(Integer listSize) {
        List<CreditHistory> historyList = new ArrayList<>();
        int pListSize = listSize != null ? listSize : LIST_SIZE;

        for (int i = 0; i < pListSize; i++) {
            historyList.add(CreditHistory.builder()
                    .creditHistoryId(new CreditHistoryId(CREDIT_HISTORY_UUID))
                    .customerId(new CustomerId(CUSTOMER_UUID))
                    .amount(new Money(CREDIT_VALUE))
                    .transactionType(sortType(i))
                    .build());
        }

        return historyList;
    }

    static public List<CreditHistory> createNOKList() {
        List<CreditHistory> historyList = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            historyList.add(CreditHistory.builder()
                    .creditHistoryId(new CreditHistoryId(CREDIT_HISTORY_UUID))
                    .customerId(new CustomerId(CUSTOMER_UUID))
                    .amount(new Money(CREDIT_VALUE))
                    .transactionType(sortType(i))
                    .build());
        }

        //Add debit only
        for (int i = 0; i < DEBIT_LIST_SIZE; i++) {
            historyList.add(CreditHistory.builder()
                    .creditHistoryId(new CreditHistoryId(CREDIT_HISTORY_UUID))
                    .customerId(new CustomerId(CUSTOMER_UUID))
                    .amount(new Money(CREDIT_VALUE))
                    .transactionType(TransactionType.DEBIT)
                    .build());
        }

        return historyList;
    }

    private static TransactionType sortType(int i) {
        if (i % ODD_DETECTOR == 0) {
            return TransactionType.CREDIT;
        }

        return TransactionType.DEBIT;
    }
}
