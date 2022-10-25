package com.h.udemy.java.uservices.order.service.domain.entity;

import com.h.udemy.java.uservices.domain.entity.BaseEntity;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public void updateWithConfirmedNameAndPrice(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
    }
}
