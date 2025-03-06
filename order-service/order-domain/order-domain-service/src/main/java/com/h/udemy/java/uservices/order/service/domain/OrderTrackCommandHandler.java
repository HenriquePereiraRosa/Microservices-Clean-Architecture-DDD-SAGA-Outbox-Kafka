package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.domain.messages.Messages;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderQuery;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.exception.OrderNotFoundException;
import com.h.udemy.java.uservices.order.service.domain.mapper.OrderDataMapper;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.OrderRepository;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;
    private final OrderRepository orderRepository;

    public OrderTrackCommandHandler(OrderDataMapper orderDataMapper, OrderRepository orderRepository) {
        this.orderDataMapper = orderDataMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        final TrackingId trackingId = new TrackingId(trackOrderQuery.getOrderTrackingId());
        Optional<Order> orderOp = orderRepository.findByTrackingId(trackingId);
        if(orderOp.isEmpty()) {
            final String msg = Messages.ERR_ORDER_TRACKING_ID_NOT_FOUND.build(trackingId);
            log.warn(msg);
            throw new OrderNotFoundException(msg);
        }

        return orderDataMapper.orderToTrackOrderResponse(orderOp.get());
    }

    public List<TrackOrderResponse> fetchAll() {

        List<Order> orders = orderRepository.fetchAll();

        return orderDataMapper.ordersToTrackOrderResponse(orders);
    }
}
