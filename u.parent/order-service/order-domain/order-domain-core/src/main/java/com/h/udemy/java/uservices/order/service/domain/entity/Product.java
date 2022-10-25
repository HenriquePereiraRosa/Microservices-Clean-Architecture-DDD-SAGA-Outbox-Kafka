package com.h.udemy.java.uservices.order.service.domain.entity;

import com.h.udemy.java.uservices.domain.entity.BaseEntity;
import com.h.udemy.java.uservices.domain.valueobject.Money;
import com.h.udemy.java.uservices.domain.valueobject.ProductId;

import java.util.UUID;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }
    public Product(ProductId productId) {
        super.setId(productId);
    }
    public Product(UUID productId) {
        this(new ProductId((productId)));
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
