package com.ecommerce.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;


public record CartResponseDto(
        @Schema UUID id,
        @Schema UUID productId,
        @Schema String productName,
        @Schema BigDecimal price,
        @Schema(example = "2") Long quantity,
        @Schema BigDecimal total // price * quantity
) {
}