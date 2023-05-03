package com.h.udemy.java.uservices.order.service.domain.saga;

import com.h.udemy.java.uservices.domain.event.EmptyEvent;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.order.service.domain.IOrderDomainService;
import com.h.udemy.java.uservices.order.service.domain.dto.message.PaymentResponse;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.event.OrderPaidEvent;
import com.h.udemy.java.uservices.order.service.domain.event.publisher.DomainEventPublisher;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderNotFoundException;
import com.h.udemy.java.uservices.order.service.domain.ports.output.message.publisher.payment.IOrderPaidRestaurantRequestRequestMessagePublisher;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.saga.ISagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

import static com.h.udemy.java.uservices.domain.messages.Messages.ERR_ORDER_NOT_FOUND;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELED_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_ID_CANCELLING;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_COMPLETING_PAYMENT_FOR_ID;
import static com.h.udemy.java.uservices.domain.messages.log.LogMessages.ORDER_PAID_FOR_ID;

@Slf4j
@Validated
@Service
public class OrderPaymentSaga implements ISagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private final IOrderDomainService orderDomainService;
    private final IOrderRepository orderRepository;
    private final IOrderPaidRestaurantRequestRequestMessagePublisher messagePublisher;

    public OrderPaymentSaga(IOrderDomainService orderDomainService,
                            IOrderRepository orderRepository,
                            IOrderPaidRestaurantRequestRequestMessagePublisher messagePublisher) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.messagePublisher = messagePublisher;
    }

    @Override
    public OrderPaidEvent process(PaymentResponse paymentResponse) {
        log.info(ORDER_COMPLETING_PAYMENT_FOR_ID.build(paymentResponse.getOrderId()));
        Order order = findOrder(paymentResponse.getOrderId());
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order, (DomainEventPublisher<OrderPaidEvent>) messagePublisher);
        orderRepository.save(order);
        log.info(ORDER_PAID_FOR_ID.build(paymentResponse.getOrderId()));
        return orderPaidEvent;
    }

    private Order findOrder(String pOrderId) {
        Optional<Order> orderOp = orderRepository.findById(new OrderId(UUID.fromString(pOrderId)));

        if(orderOp.isEmpty()) {
            final String message = ERR_ORDER_NOT_FOUND.build(pOrderId);
            log.error(message);
            throw new OrderNotFoundException(message);
        }

        return orderOp.get();
    }

    @Override
    public EmptyEvent rollback(PaymentResponse paymentResponse) {
        log.info(ORDER_ID_CANCELLING.build(paymentResponse.getOrderId()));
        Order order = findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order,
                paymentResponse.getFailureMessages());
        orderRepository.save(order);
        log.info(ORDER_ID_CANCELED_ID.build(paymentResponse.getOrderId()));
        return EmptyEvent.INSTANCE;
    }
}
