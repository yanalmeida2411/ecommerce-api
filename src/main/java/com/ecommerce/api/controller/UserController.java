package com.ecommerce.api.controller;

import com.ecommerce.api.dtos.user.UserRequestDto;
import com.ecommerce.api.dtos.user.UserResponseDto;
import com.ecommerce.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Busca todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso não autorizado.\"}")
                    )),
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> listAll() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @Operation(summary = "Busca um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso não autorizado.\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Usuário com ID informado não existe\"}")
                    )
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @Operation(summary = "Cria um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já cadastrado",
                    content = @Content(mediaType = "application/json",
                            examples =
                            @ExampleObject(value = "{ \"message\": \"Este e-mail já está em uso ou a senha deve ter no mínimo 6 caracteres\"}")
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userDto) {
        return ResponseEntity.status(201).body(userService.createUser(userDto));
    }

    @Operation(summary = "Atualiza os dados de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso não autorizado.\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Não foi possível atualizar: usuário inexistente\"}")
                    )
            )
    })
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID userId, @Valid @RequestBody UserRequestDto user) {
        return ResponseEntity.ok(userService.updatingUser(userId, user));
    }

    @Operation(summary = "Deleta um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso não autorizado.\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Tentativa de exclusão falhou: ID não encontrado\"}")
                    )
            )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

}