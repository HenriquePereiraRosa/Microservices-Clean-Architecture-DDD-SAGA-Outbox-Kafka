package com.h.udemy.java.uservices.order.service.domain.dto.create;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateOrderCommand(@NotNull UUID customerId,
                                 @NotNull UUID restaurantId,
                                 @NotNull BigDecimal price,
                                 @NotNull List<OrderItemDTO> items,
                                 @NotNull OrderAddressDTO address) {

}
