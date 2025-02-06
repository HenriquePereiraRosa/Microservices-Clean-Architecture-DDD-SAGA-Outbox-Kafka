package com.h.udemy.java.uservices.constants;

import java.math.BigDecimal;
import java.util.UUID;

public interface TestConstants {

    UUID ORDER_ID = UUID.fromString("efcf7559-cb80-41fe-991e-1a60b68b0532");
    UUID TRACKING_ID = UUID.fromString("89b0b3e0-7a3a-4a3b-9938-c7cbd090b074");
    UUID STREET_ADDRESS_ID = UUID.fromString("9e672465-f656-48c0-8c96-d0ecc631f32c");
    UUID CUSTOMER_ID = UUID.fromString("6c1cdc4d-2b8f-4582-b765-349931ddb653");
    UUID RESTAURANT_ID = UUID.fromString("e42502d3-6099-44ad-bcbe-e30f85ec9d9e");
    UUID PRODUCT_ID = UUID.fromString("ae15c1e1-86a9-48cf-87fd-11f5b16c404a");
    UUID SAGA_ID = UUID.fromString("ae15c1e1-86a9-48cf-87fd-11f5b16c404a");
    UUID PAYMENT_ID = UUID.fromString("ae15c1e1-86a9-48cf-87fd-11f5b16c404a");
    UUID CREDIT_ENTRY_ID = UUID.randomUUID();
    UUID CREDIT_HISTORY_ID = UUID.randomUUID();

    BigDecimal PRICE = new BigDecimal("100");
    double ERROR_PRICE = -99.999;

    int VERSION = 1;
}
