package com.ecommerce.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDto(
        @Schema(example = "62h3g5jh2315hb") UUID id,
        @Schema(example = "João") @NotBlank String firstName,
        @Schema(example = "Silva") @NotBlank String lastName,
        @Schema(example = "joao@email.com") @Email @NotBlank String email,
        @Schema(example = "21999999999") @NotBlank String phone,
        @Schema(example = "2000-01-01") @NotNull LocalDate birthDate,
        @Schema(example = "Masculino") @NotBlank String gender,
        @Schema(example = "Rua das Flores, 123") @NotBlank String address
) {
}