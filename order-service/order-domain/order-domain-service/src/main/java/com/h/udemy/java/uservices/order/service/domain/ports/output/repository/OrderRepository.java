package com.h.udemy.java.uservices.order.service.domain.ports.output.repository;

import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order insertOrder(Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId);
    Optional<Order> findById(OrderId orderId);

    List<Order> fetchAll();

    Order save(Order order);
}
