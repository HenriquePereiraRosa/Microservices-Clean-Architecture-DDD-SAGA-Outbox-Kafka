package com.h.udemy.java.uservices.payment.domain.core.test.util.factory;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.payment.domain.core.entity.CreditHistory;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.CreditEntryId;
import com.h.udemy.java.uservices.payment.domain.core.valueobject.TransacionType;

import java.util.ArrayList;
import java.util.List;

import static com.h.udemy.java.uservices.payment.domain.core.test.Const.CREDIT_ID;
import static com.h.udemy.java.uservices.payment.domain.core.test.Const.CUSTOMER_ID;

public class CreditHistoryFactory {

    private static final double CREDIT_VALUE = 1002.22;
    public static final int LIST_SIZE = 10;
    public static final int DEBIT_LIST_SIZE = 3;
    public static final int ODD_DETECTOR = 2;

    static public List<CreditHistory> createOKList() {
        List<CreditHistory> historyList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            historyList.add(CreditHistory.builder()
                            .creditEntryId(new CreditEntryId(CREDIT_ID))
                            .customerId(new CustomerId(CUSTOMER_ID))
                            .amount(new Money(CREDIT_VALUE))
                            .transacionType(sortType(i))
                    .build());
        }

        return historyList;
    }

    static public List<CreditHistory> createNOKList() {
        List<CreditHistory> historyList = new ArrayList<>();
        for(int i = 0; i < LIST_SIZE; i++) {
            historyList.add(CreditHistory.builder()
                    .creditEntryId(new CreditEntryId(CREDIT_ID))
                    .customerId(new CustomerId(CUSTOMER_ID))
                    .amount(new Money(CREDIT_VALUE))
                    .transacionType(sortType(i))
                    .build());
        }

        //Add debit only
        for(int i = 0; i < DEBIT_LIST_SIZE; i++) {
            historyList.add(CreditHistory.builder()
                    .creditEntryId(new CreditEntryId(CREDIT_ID))
                    .customerId(new CustomerId(CUSTOMER_ID))
                    .amount(new Money(CREDIT_VALUE))
                    .transacionType(TransacionType.DEBIT)
                    .build());
        }

        return historyList;
    }

    private static TransacionType sortType(int i) {
        if(i % ODD_DETECTOR == 0) {
            return TransacionType.DEBIT;
        }

        return TransacionType.CREDIT;
    }
}
