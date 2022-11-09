package com.h.udemy.java.uservices.rest;

import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderQuery;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.ports.input.service.IOrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.*;

@Slf4j
@RestController
@RequestMapping(value = "/orders",
    produces = "application/vnd.api.v1+json")
public class OrderController {

    private final IOrderApplicationService orderApplicationService;

    public OrderController(IOrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {

        log.info(ORDER_ID_CREATING.get(),
                createOrderCommand.getCustomerId(),
                createOrderCommand.getRestaurantId());

        CreateOrderResponse createOrderResponse = orderApplicationService
                .createOrder(createOrderCommand);

        log.info(ORDER_ID_CREATED_SUCCESSFULLY.get(),
                createOrderResponse.getTrackingId());

        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/by-tracking-id")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@RequestParam UUID trackingId) {

        TrackOrderResponse trackOrderResponse = orderApplicationService
                .trackOrder(TrackOrderQuery
                        .builder()
                        .orderTrackingId(trackingId)
                        .build());

        log.info(ORDER_TRACKING_BY_TRACKING_ID.get(), trackOrderResponse.getOrderTrackingId());

        return ResponseEntity.ok(trackOrderResponse);

    }
}
