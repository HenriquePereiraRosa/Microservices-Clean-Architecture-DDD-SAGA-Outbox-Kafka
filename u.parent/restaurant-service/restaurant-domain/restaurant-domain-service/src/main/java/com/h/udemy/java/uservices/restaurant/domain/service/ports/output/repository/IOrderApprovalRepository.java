package com.h.udemy.java.uservices.restaurant.domain.service.ports.output.repository;

import com.h.udemy.java.uservices.restaurant.domain.core.entity.OrderApproval;

public interface IOrderApprovalRepository {

    OrderApproval save(OrderApproval orderApproval);

}
