package com.h.udemy.java.uservices.order.service.dataaccess.customer.mapper;

import com.h.udemy.java.uservices.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.h.udemy.java.uservices.order.service.domain.valueobject.StreetAddress;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-04T14:46:59+0000",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.5 (Eclipse Adoptium)"
)
@Component
public class IOrderDataAccessMapperImpl extends IOrderDataAccessMapper {

    @Override
    StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
        if ( address == null ) {
            return null;
        }

        UUID id = null;
        String street = null;
        String postalCode = null;
        String city = null;

        id = address.getId();
        street = address.getStreet();
        postalCode = address.getPostalCode();
        city = address.getCity();

        StreetAddress streetAddress = new StreetAddress( id, street, postalCode, city );

        return streetAddress;
    }

    @Override
    OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        if ( deliveryAddress == null ) {
            return null;
        }

        OrderAddressEntity.OrderAddressEntityBuilder orderAddressEntity = OrderAddressEntity.builder();

        orderAddressEntity.id( deliveryAddress.id() );
        orderAddressEntity.street( deliveryAddress.street() );
        orderAddressEntity.postalCode( deliveryAddress.postalCode() );
        orderAddressEntity.city( deliveryAddress.city() );

        return orderAddressEntity.build();
    }
}
