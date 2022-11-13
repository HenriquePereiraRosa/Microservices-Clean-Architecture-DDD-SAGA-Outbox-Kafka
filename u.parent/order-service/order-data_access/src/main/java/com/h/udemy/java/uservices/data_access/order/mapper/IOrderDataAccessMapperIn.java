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
public interface IOrderDataAccessMapperIn {

    @Mapping(target="id", source="order.id.value")
    @Mapping(target="customerId", source="order.customerId.value")
    @Mapping(target="restaurantId", source="order.restaurantId.value")
    @Mapping(target="trackingId", source="order.trackingId.value")
    @Mapping(target="address", expression = "java(toAddressEntity(order.getDeliveryAddress()")
    @Mapping(target="items", expression = "java(toOrderItemEntity(order.getItems()")
    @Mapping(target="failureMessages", expression = "java(order.concatFailureMessages()")
    OrderEntity orderToOrderEntity(Order order);

    List<OrderItemEntity> toOrderItemEntity(List<OrderItem> items);

    OrderAddressEntity toAddressEntity(StreetAddress streetAddress);

    OrderAddressEntity setOrderEntityInOrderItemsList(Order order);
}
