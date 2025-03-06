package com.h.udemy.java.uservices.restaurant.domain.service.dto;

import com.h.udemy.java.uservices.domain.valueobject.RestaurantOrderStatus;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalRequest {

    private String id;
    private String sagaId;
    private String restaurantId;
    private String orderId;
    private RestaurantOrderStatus restaurantOrderStatus;
    private List<Product> products;
    private BigDecimal price;
    private Instant createdAt;
}
