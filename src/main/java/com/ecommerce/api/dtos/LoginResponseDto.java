package com.ecommerce.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto(
        @Schema(example = "Login realizado com sucesso!")
        String message,

        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {
}