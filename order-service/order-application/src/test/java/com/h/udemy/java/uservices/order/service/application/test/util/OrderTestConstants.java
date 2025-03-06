package com.h.udemy.java.uservices.order.service.application.test.util;

import com.h.udemy.java.uservices.constants.TestConstants;
import com.h.udemy.java.uservices.domain.valueobject.CustomerId;
import com.h.udemy.java.uservices.domain.valueobject.OrderId;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;
import com.h.udemy.java.uservices.domain.valueobject.RestaurantId;
import com.h.udemy.java.uservices.order.service.domain.valueobject.TrackingId;

import java.util.UUID;

public class OrderTestConstants {

    public static final OrderId ORDER_ID = new OrderId(TestConstants.ORDER_ID);
    public static final TrackingId TRACKING_ID = new TrackingId(TestConstants.TRACKING_ID);
    public static final UUID STREET_ADDRESS_ID = TestConstants.STREET_ADDRESS_ID;
    public static final CustomerId CUSTOMER_ID = new CustomerId(TestConstants.CUSTOMER_ID);
    public static final RestaurantId RESTAURANT_ID = new RestaurantId(TestConstants.RESTAURANT_ID);
    public static final ProductId PRODUCT_ID = new ProductId(TestConstants.PRODUCT_ID);
}
