package com.h.udemy.java.uservices.order.service.domain;

import com.h.udemy.java.uservices.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.h.udemy.java.uservices.order.service.domain.ports.input.message.listener.restaurantApproval.IRestaurantApprovalMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListener implements IRestaurantApprovalMessageListener {
    
    @Override
    public void orderApproval(RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}
