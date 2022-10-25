package com.h.udemy.java.uservices.order.service.domain.dto.message;

import com.h.udemy.java.uservices.domain.valueobject.OrderApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalResponse {

    private final String id;
    private final String sagaId;
    private final String orderId;
    private final String restaurantId;
    private final Instant createdAt;
    private final OrderApprovalStatus orderApprovalStatus;
    private final List<String> failureMessages;


}
