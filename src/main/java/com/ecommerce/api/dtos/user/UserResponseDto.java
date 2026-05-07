package com.ecommerce.api.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDto(
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID id,
        @Schema(example = "João") @NotBlank String firstName,
        @Schema(example = "Silva") @NotBlank String lastName,
        @Schema(example = "joao@email.com") @Email @NotBlank String email,
        @Schema(example = "21999999999") @NotBlank String phone,
        @Schema(example = "2000-01-01") @NotNull LocalDate birthDate,
        @Schema(example = "Masculino") @NotBlank String gender,
        @Schema(example = "Rua das Flores, 123") @NotBlank String address
) {
}