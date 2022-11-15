package com.h.udemy.java.uservices.dataaccess.order.adapter;

import com.h.udemy.java.uservices.dataaccess.order.mapper.OrderDataAccessMapper;
import com.h.udemy.java.uservices.dataaccess.order.repository.IOrderJpaRepository;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepository implements IOrderRepository {

    private final IOrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepository(IOrderJpaRepository orderJpaRepository,
                           OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order insertOrder(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(orderJpaRepository
                .save(orderDataAccessMapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }
}
