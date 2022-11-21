package com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.restaurantApproval;

import com.h.udemy.java.uservices.order.service.domain.dto.message.RestaurantApprovalResponse;

public interface IRestaurantApprovalMessageListener {

    void orderApproval(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
