package com.h.udemy.java.uservices.order.service.domain.saga;

import com.h.udemy.java.uservices.domain.event.EmptyEvent;
import com.h.udemy.java.uservices.order.service.domain.IOrderDomainService;
import com.h.udemy.java.uservices.order.service.domain.dto.message.PaymentResponse;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import com.h.udemy.java.uservices.order.service.domain.event.publisher.DomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.IOrderPaidRestaurantRequestRequestMessagePublisher;
import com.h.udemy.java.uservices.order.service.domain.saga.helper.OrderSagaHelper;
import com.h.udemy.java.uservices.saga.ISagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_COMPLETING_PAYMENT_FOR_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELED_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELLING;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_PAID_FOR_ID;

@Slf4j
@Component
public class OrderPaymentSaga implements ISagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private final IOrderDomainService orderDomainService;
    private final OrderSagaHelper sagaHelper;
    private final IOrderPaidRestaurantRequestRequestMessagePublisher messagePublisher;

    public OrderPaymentSaga(IOrderDomainService orderDomainService,
                            OrderSagaHelper sagaHelper,
                            IOrderPaidRestaurantRequestRequestMessagePublisher messagePublisher) {
        this.orderDomainService = orderDomainService;
        this.sagaHelper = sagaHelper;
        this.messagePublisher = messagePublisher;
    }

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse paymentResponse) {
        log.info(ORDER_COMPLETING_PAYMENT_FOR_ID.build(paymentResponse.getOrderId()));
        Order order = sagaHelper.findOrder(paymentResponse.getOrderId());
        OrderPaidEvent orderPaidEvent = orderDomainService
                .payOrder(order, (DomainEventPublisher<OrderPaidEvent>) messagePublisher);
        sagaHelper.saveOrder(order);
        log.info(ORDER_PAID_FOR_ID.build(paymentResponse.getOrderId()));
        return orderPaidEvent;
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse paymentResponse) {
        log.info(ORDER_ID_CANCELLING.build(paymentResponse.getOrderId()));
        Order order = sagaHelper.findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order,
                paymentResponse.getFailureMessages());
        sagaHelper.saveOrder(order);
        log.info(ORDER_ID_CANCELED_ID.build(paymentResponse.getOrderId()));
        return EmptyEvent.INSTANCE;
    }
}
