package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    public TrackOrderResponse trackOrder(CreateOrderCommand createOrderCommand) {
        
    }
}
