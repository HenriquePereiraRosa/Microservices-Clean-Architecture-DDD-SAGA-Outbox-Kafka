package com.h.udemy.java.uservices.test.util;

import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ConstTestUtils {

    public static final OrderId ORDER_ID = new OrderId(UUID
            .fromString("efcf7559-cb80-41fe-991e-1a60b68b0532"));
    public static final TrackingId TRACKING_ID = new TrackingId(UUID
            .fromString("89b0b3e0-7a3a-4a3b-9938-c7cbd090b074"));
    public static final UUID STREET_ADDRESS_ID = UUID
            .fromString("9e672465-f656-48c0-8c96-d0ecc631f32c");
    public static final CustomerId CUSTOMER_ID = new CustomerId(UUID
            .fromString("6c1cdc4d-2b8f-4582-b765-349931ddb653"));
    public static final RestaurantId RESTAURANT_ID = new RestaurantId(UUID
            .fromString("e42502d3-6099-44ad-bcbe-e30f85ec9d9e"));
    public static final ProductId PRODUCT_ID = new ProductId(UUID
            .fromString("ae15c1e1-86a9-48cf-87fd-11f5b16c404a"));

    public static final ZonedDateTime TIME_ZONE = ZonedDateTime.now(ZoneId.of("UTC"));
}
