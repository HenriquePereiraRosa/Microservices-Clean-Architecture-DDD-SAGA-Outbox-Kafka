package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderQuery;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.ports.input.service.IOrderAplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
class OrderAplicationService implements IOrderAplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;

    private final OrderTrackCommandHandler orderTrackCommandHandler;

    public OrderAplicationService(OrderCreateCommandHandler orderCreateCommandHandler,
                                  OrderTrackCommandHandler orderTrackCommandHandler) {
        this.orderCreateCommandHandler = orderCreateCommandHandler;
        this.orderTrackCommandHandler = orderTrackCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return null;
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return null;
    }
}
