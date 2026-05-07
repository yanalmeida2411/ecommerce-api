package com.ecommerce.api.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @Schema(example = "joao@email.com", description = "E-mail do usuário cadastrado")
        @NotBlank(message = "Email é obrigatório")
        String email,

        @Schema(example = "Senha@123", description = "Senha do usuário")
        @NotBlank(message = "Senha é obrigatório")
        String password) {
}