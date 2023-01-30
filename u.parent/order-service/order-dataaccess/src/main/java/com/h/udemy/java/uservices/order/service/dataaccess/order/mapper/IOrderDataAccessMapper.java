package com.h.udemy.java.uservices.order.service.dataaccess.order.mapper;

import com.h.udemy.java.uservices.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;


//@Mapper(componentModel = "spring",
//        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
//public abstract class IOrderDataAccessMapper {
//
//    abstract StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address);
//
//    abstract OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress);
//}
