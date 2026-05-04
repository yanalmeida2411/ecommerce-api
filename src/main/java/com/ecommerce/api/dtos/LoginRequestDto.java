package com.ecommerce.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @Schema(example = "joao@email.com", description = "E-mail do usuário cadastrado")
        @Email @NotBlank
        String email,

        @Schema(example = "Senha@123", description = "Senha do usuário")
        @NotBlank String password) {
}