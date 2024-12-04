package com.h.udemy.java.uservices.rest.v1;

import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderCommand;
import com.h.udemy.java.uservices.order.service.domain.dto.create.CreateOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderQuery;
import com.h.udemy.java.uservices.order.service.domain.dto.track.TrackOrderResponse;
import com.h.udemy.java.uservices.order.service.domain.ports.input.service.IOrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.ORDER_ID_CREATED_SUCCESSFULLY;
import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.ORDER_ID_CREATING;
import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.ORDER_TRACKING_ALL;
import static com.h.udemy.java.uservices.order.service.domain.messages.log.LogMessages.ORDER_TRACKING_BY_TRACKING_ID;

@Slf4j
@RestController
@RequestMapping(value = "/v1/orders",
    produces = "application/vnd.api.v1+json")
public class OrderControllerV1 {

    private final IOrderApplicationService orderApplicationService;

    public OrderControllerV1(IOrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {

        log.info(ORDER_ID_CREATING.get(),
                createOrderCommand.customerId(),
                createOrderCommand.restaurantId());

        CreateOrderResponse createOrderResponse = orderApplicationService
                .createOrder(createOrderCommand);

        log.info(ORDER_ID_CREATED_SUCCESSFULLY.get(),
                createOrderResponse.getTrackingId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createOrderResponse);
    }

    @GetMapping("/by-tracking-id")
    public ResponseEntity<TrackOrderResponse> fetchOrderByTrackingId(@RequestParam UUID trackingId) {

        TrackOrderResponse trackOrderResponse = orderApplicationService
                .trackOrder(TrackOrderQuery
                        .builder()
                        .orderTrackingId(trackingId)
                        .build());

        log.info(ORDER_TRACKING_BY_TRACKING_ID.get(), trackOrderResponse.getOrderTrackingId());

        return ResponseEntity.ok(trackOrderResponse);

    }

    @GetMapping
    public ResponseEntity<List<TrackOrderResponse>> fetchAllOrder() {

        List<TrackOrderResponse> trackOrderResponseList = orderApplicationService
                .fetchAllOrders();

        log.info(ORDER_TRACKING_ALL.get(), trackOrderResponseList.stream().count());

        return ResponseEntity.ok(trackOrderResponseList);

    }
}
