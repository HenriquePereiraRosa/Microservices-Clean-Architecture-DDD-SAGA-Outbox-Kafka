package com.h.udemy.java.uservices.restaurant.dataaccess.adapter;


import com.h.udemy.java.uservices.restaurant.dataaccess.mapper.RestaurantDataAccessMapper;
import com.h.udemy.java.uservices.restaurant.dataaccess.repository.IOrderApprovalJpaRepository;
import com.h.udemy.java.uservices.restaurant.domain.core.entity.OrderApproval;
import com.h.udemy.java.uservices.restaurant.domain.service.ports.output.repository.IOrderApprovalRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderApprovalRepository implements IOrderApprovalRepository {

    private final IOrderApprovalJpaRepository orderApprovalJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public OrderApprovalRepository(IOrderApprovalJpaRepository orderApprovalJpaRepository,
                                   RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.orderApprovalJpaRepository = orderApprovalJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return restaurantDataAccessMapper
                .orderApprovalEntityToOrderApproval(orderApprovalJpaRepository
                        .save(restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
    }

}
