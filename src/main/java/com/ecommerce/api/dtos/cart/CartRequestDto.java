package com.ecommerce.api.dtos.cart;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CartRequestDto(
        @Schema(example = "cba5f339-0e3c-47d7-b4f9-fdab5d73014f") UUID productId,
        @Schema(example = "2") Long quantity
) {
}
