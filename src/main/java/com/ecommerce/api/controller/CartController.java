package com.ecommerce.api.controller;

import com.ecommerce.api.dtos.CartRequestDto;
import com.ecommerce.api.dtos.CartUpdateQuantityDto;
import com.ecommerce.api.dtos.FullCartResponseDto;
import com.ecommerce.api.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@Tag(name = "Cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Cria um novo carrinho por usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carrinho criado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Token ausente ou expirado\"}")
                    )
            )
    })
    @PostMapping()
    public ResponseEntity<FullCartResponseDto> addItem(
            @RequestBody @Valid CartRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addToCart(dto));
    }

    @Operation(summary = "Busca carrinhos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrinho encontrado.")
            ,
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Token ausente ou expirado\"}")
                    )
            )
    })
    @GetMapping()
    public ResponseEntity<FullCartResponseDto> getMyCart() {
        return ResponseEntity.ok(cartService.findCartTotalByUser());
    }

    @Operation(summary = "Atualiza um carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrinho atualizado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Token ausente ou expirado\"}")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado para atualização",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Erro ao deletar: ID inválido\"}")
                    )
            )
    })
    @PatchMapping("/upateQuantity")
    public ResponseEntity<FullCartResponseDto> updateQuantity(
            @Valid @RequestBody CartUpdateQuantityDto quantity) {

        FullCartResponseDto result = cartService.updateCartQuantity(quantity);

        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta um produto do carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Token ausente ou expirado\"}")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado para exclusão",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Erro ao deletar: ID inválido\"}")
                    )
            )
    })
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable UUID productId) {
        cartService.removeProductCompletely(productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta todos os produtos do carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produtos removidos do carrinho removido"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Token ausente ou expirado\"}")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Carrinho não encontrado para exclusão",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Erro ao deletar: ID inválido\"}")
                    )
            )
    })
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearFullCart();
        return ResponseEntity.noContent().build();
    }
}
