package com.h.udemy.java.uservices.restaurant.domain.service.ports.input.message.listener;

import com.h.udemy.java.uservices.restaurant.domain.service.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {

    void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest);

}
