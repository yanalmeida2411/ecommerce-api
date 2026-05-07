package com.ecommerce.api.dtos.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponseDto(
        UUID productId,
        String productName,
        Long quantity,
        BigDecimal priceAtPurchase
) {
}
