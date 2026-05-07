package com.ecommerce.api.dtos.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDto(
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID id,
        @Schema(example = "Teclado Mecânico RGB") @NotBlank String name,
        @Schema(example = "250") @Positive @NotNull BigDecimal price,
        @Schema(example = "Periféricos") @NotBlank String category,
        @Schema(example = "50") @Min(0) @NotNull Long quantity,
        @Schema(example = "xxxxx") @NotNull String product_image,
        @Schema(example = "descricao xxxx") @NotNull String description
) {
}
