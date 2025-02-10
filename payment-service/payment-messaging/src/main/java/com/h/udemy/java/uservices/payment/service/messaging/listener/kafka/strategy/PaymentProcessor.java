package com.h.udemy.java.uservices.payment.service.messaging.listener.kafka.strategy;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ERR_RETHROWN_DATA_ACCESS_EXCEPTION;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ERR_UNIQUE_VIOLATION_IN_REQUEST_LISTENER;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.PAYMENT_ERR_ID_NOT_FOUND;
import static java.util.Objects.nonNull;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;

import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.h.udemy.java.uservices.domain.valueobject.PaymentOrderStatus;
import com.h.udemy.java.uservices.kafka.order.avro.model.PaymentRequestAvroModel;
import com.h.udemy.java.uservices.payment.domain.core.exception.PaymentNotFoundException;
import com.h.udemy.java.uservices.payment.domain.service.PaymentRequestMessageListener;
import com.h.udemy.java.uservices.payment.domain.service.dto.PaymentRequest;
import com.h.udemy.java.uservices.payment.domain.service.exception.PaymentDomainServiceException;
import com.h.udemy.java.uservices.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentProcessor {
    private final Map<PaymentOrderStatus, PaymentOrderStatusStrategy> strategies = new EnumMap<>(PaymentOrderStatus.class);
    private final PaymentRequestMessageListener paymentRequestListener;
    private final PaymentMessagingDataMapper mapper;

    public PaymentProcessor(PaymentRequestMessageListener paymentRequestListener,
            PaymentMessagingDataMapper mapper) {
        this.paymentRequestListener = paymentRequestListener;
        this.mapper = mapper;

        strategies.put(PaymentOrderStatus.PENDING, new PaymentCompletedOrderStatusStrategy());
        strategies.put(PaymentOrderStatus.CANCELLED, new PaymentCancelledOrderStatusStrategy());
    }

    public void processPayment(PaymentRequestAvroModel paymentRequestAvroModel) {
        try {
            PaymentRequest paymentRequest = mapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel);
            PaymentOrderStatus paymentOrderStatus = paymentRequest.getPaymentOrderStatus();

            strategies.getOrDefault(
                            paymentOrderStatus,
                            new DefaultPaymentOrderStatusStrategy())
                    .processPayment(paymentRequestListener, paymentRequest);
        } catch (DataAccessException e) {
            SQLException sqlException = (SQLException) e.getCause();
            if (nonNull(sqlException) && !PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                throw new PaymentDomainServiceException(ERR_RETHROWN_DATA_ACCESS_EXCEPTION.build(e.getMessage()), e);
            }
                //NO-OP for unique constraint exception
                log.error(ERR_UNIQUE_VIOLATION_IN_REQUEST_LISTENER.build(
                        sqlException.getSQLState(),
                        PaymentRequestMessageListener.class.getSimpleName(),
                        paymentRequestAvroModel.getOrderId()));
        } catch (PaymentNotFoundException e) {
            //NO-OP for PaymentNotFoundException
            log.error(PAYMENT_ERR_ID_NOT_FOUND.build(paymentRequestAvroModel.getOrderId()));
        }
    }
}
