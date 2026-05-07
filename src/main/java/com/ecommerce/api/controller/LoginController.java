package com.ecommerce.api.controller;

import com.ecommerce.api.dtos.user.LoginRequestDto;
import com.ecommerce.api.dtos.user.LoginResponseDto;
import com.ecommerce.api.service.LoginService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "Realiza o login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"E-mail ou senha incorretos\"}")
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto body) {
        LoginResponseDto response = loginService.authenticate(body);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Verifica se o usuário atual está autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Acesso autorizado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"token\": \"Token válido e ativo\", \"user\": \"usuario@email.com\" }")
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Token inválido ou expirado\"}")
                    )
            )
    })
    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateToken() {

        var authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();

        String email = authentication.getName();

        return ResponseEntity.ok(Map.of(
                "status", "Token válido e ativo",
                "user", email
        ));
    }
}
