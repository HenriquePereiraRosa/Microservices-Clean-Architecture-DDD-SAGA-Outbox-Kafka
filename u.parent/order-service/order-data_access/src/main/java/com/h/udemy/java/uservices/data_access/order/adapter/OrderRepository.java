package com.h.udemy.java.uservices.data_access.order.adapter;

import com.h.udemy.java.uservices.data_access.order.mapper.IOrderDataAccessMapperIn;
import com.h.udemy.java.uservices.data_access.order.mapper.IOrderDataAccessMapperOut;
import com.h.udemy.java.uservices.data_access.order.repository.IOrderJpaRepository;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.ports.output.repository.IOrderRepository;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepository implements IOrderRepository {

    private final IOrderJpaRepository orderJpaRepository;
    private final IOrderDataAccessMapperIn orderMapperIn;
    private final IOrderDataAccessMapperOut orderMapperOut;

    public OrderRepository(IOrderJpaRepository orderJpaRepository,
                           IOrderDataAccessMapperIn orderMapperIn,
                           IOrderDataAccessMapperOut orderMapperOut) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderMapperIn = orderMapperIn;
        this.orderMapperOut = orderMapperOut;
    }


    @Override
    public Order insertOrder(Order order) {
        return orderMapperOut.orderEntityToOrder(orderJpaRepository
                .save(orderMapperIn.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderMapperOut::orderEntityToOrder);
    }
}
