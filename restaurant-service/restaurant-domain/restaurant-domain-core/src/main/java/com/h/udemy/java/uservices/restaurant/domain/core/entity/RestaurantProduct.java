package com.h.udemy.java.uservices.restaurant.domain.core.entity;

import com.h.udemy.java.uservices.domain.entity.BaseEntity;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;

public class RestaurantProduct extends BaseEntity<ProductId> {

    private String name;
    private Money price;
    private final int quantity;
    private boolean available;

    private RestaurantProduct(Builder builder) {
        setId(builder.productId);
        name = builder.name;
        price = builder.price;
        quantity = builder.quantity;
        available = builder.available;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ProductId productId;
        private String name;
        private Money price;
        private int quantity;
        private boolean available;

        private Builder() {
        }


        public Builder productId(ProductId val) {
            productId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder available(boolean val) {
            available = val;
            return this;
        }

        public RestaurantProduct build() {
            return new RestaurantProduct(this);
        }
    }


    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return available;
    }


    public void updateWithConfirmedNamePriceAndAvailability(String name,
                                                            Money price,
                                                            boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
    }
}