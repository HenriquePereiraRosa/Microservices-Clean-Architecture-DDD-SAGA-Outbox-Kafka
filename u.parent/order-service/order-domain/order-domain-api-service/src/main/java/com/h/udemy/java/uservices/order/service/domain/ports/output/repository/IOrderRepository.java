package com.h.udemy.java.uservices.order.service.domain.ports.output.repository;

import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository {

    Order insertOrder(Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId);

    List<Order> fetchAll();
}
