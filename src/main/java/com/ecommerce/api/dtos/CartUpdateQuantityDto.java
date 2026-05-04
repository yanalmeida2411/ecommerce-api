package com.ecommerce.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CartUpdateQuantityDto(
        @Schema(example = "cba5f339-0e3c-47d7-b4f9-fdab5d73014f") UUID productId,
        @NotNull
        @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
        @Schema(example = "2")
        Long quantity
) {
}