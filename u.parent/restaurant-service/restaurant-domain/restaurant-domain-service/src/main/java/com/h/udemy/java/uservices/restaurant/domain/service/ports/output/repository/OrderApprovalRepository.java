package com.h.udemy.java.uservices.restaurant.domain.service.ports.output.repository;

import com.h.udemy.java.uservices.restaurant.domain.core.entity.OrderApproval;

public interface OrderApprovalRepository {

    OrderApproval save(OrderApproval orderApproval);

}
