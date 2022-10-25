package com.h.udemy.java.uservices.order.service.domain.ports.input.service;

import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderQuery;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderResponse;

import javax.validation.Valid;

public interface IOrderAplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
