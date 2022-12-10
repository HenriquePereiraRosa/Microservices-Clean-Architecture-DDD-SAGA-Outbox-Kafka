package com.h.udemy.java.uservices.order.service.domain.dto.create;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItemDTO(@NotNull UUID productId,
                           @NotNull Integer quantity,
                           @NotNull BigDecimal price) {

    public BigDecimal getSubtotal() {
        return price.multiply(new BigDecimal(quantity));
    }

}
