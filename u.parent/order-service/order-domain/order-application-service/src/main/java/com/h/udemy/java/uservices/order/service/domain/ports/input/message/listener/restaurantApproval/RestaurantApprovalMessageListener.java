package com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.restaurantApproval;

import com.h.udemy.java.uservices.order.service.domain.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalMessageListener {

    void orderApproval(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
