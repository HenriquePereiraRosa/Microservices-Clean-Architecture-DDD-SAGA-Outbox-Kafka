package com.h.udemy.java.uservices.data_access.order.mapper;

import com.h.udemy.java.uservices.data_access.order.entity.OrderAddressEntity;
import com.h.udemy.java.uservices.data_access.order.entity.OrderEntity;
import com.h.udemy.java.uservices.data_access.order.entity.OrderItemEntity;
import com.h.udemy.java.uservices.order.service.domain.entity.Order;
import com.h.udemy.java.uservices.order.service.domain.entity.OrderItem;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderDataAccessMapperOut {

    @Mapping(target="id", source="orderEntity.id.value")
    @Mapping(target="customerId", source="orderEntity.customerId.value")
    @Mapping(target="restaurantId", source="orderEntity.restaurantId.value")
    @Mapping(target="trackingId", source="orderEntity.trackingId.value")
    @Mapping(target="address", expression = "java(toAddressEntity(order.getDeliveryAddress()")
    @Mapping(target="items", expression = "java(toOrderItemEntity(order.getItems()")
    @Mapping(target="failureMessages", expression = "java(order.concatFailureMessages()")
    @Mapping(target="address.order", source = "orderEntity")
    @Mapping(target=".", expression = "java(order.concatFailureMessages(., orderEntity)")
    Order orderEntityToOrder(OrderEntity orderEntity);

    List<OrderItem> orderItemEntitiesToOrderItems(List<OrderItemEntity> items);

    StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity orderAddressEntity);

    private OrderEntity addEntityToEachItem(Order order, OrderEntity orderEntity) {
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
        return orderEntity;
    }

}
