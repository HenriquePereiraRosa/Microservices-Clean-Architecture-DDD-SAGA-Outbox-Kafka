package com.h.udemy.java.uservices.restaurant.domain.service;

import com.h.udemy.java.uservices.restaurant.domain.core.event.OrderApprovalEvent;
import com.h.udemy.java.uservices.restaurant.domain.service.dto.RestaurantApprovalRequest;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.input.message.listener.IRestaurantApprovalRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RestaurantApprovalRequestMessageListener implements IRestaurantApprovalRequestMessageListener {

    private final RestaurantApprovalRequestHelper approvalRequestHelper;

    public RestaurantApprovalRequestMessageListener(RestaurantApprovalRequestHelper
                                                            approvalRequestHelper) {

        this.approvalRequestHelper = approvalRequestHelper;
    }

    @Override
    public void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        OrderApprovalEvent orderApprovalEvent = approvalRequestHelper
                .persistOrderApproval(restaurantApprovalRequest);

        orderApprovalEvent.fire();
    }
}
