package com.ecommerce.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRequestDto(
        @Schema(example = "João") @NotBlank String firstName,

        @Schema(example = "Silva") @NotBlank String lastName,

        @Schema(example = "joao@email.com") @Email @NotBlank String email,

        @Schema(example = "Senha@123")
        @NotBlank(message = "A senha é obrigatória")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.{6,}).*$",
                message = "A senha deve conter no mínimo 6 caracteres, uma letra maiúscula e um símbolo"
        ) String password,

        @Schema(example = "21999999999") @NotBlank String phone,

        @Schema(example = "2000-01-01") @NotNull LocalDate birthDate,

        @Schema(example = "Masculino") @NotBlank String gender,

        @Schema(example = "Rua das Flores, 123") @NotBlank String address
) {
}