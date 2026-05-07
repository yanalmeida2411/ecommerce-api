package com.ecommerce.api.controller;

import com.ecommerce.api.dtos.order.FullOrderResponseDto;
import com.ecommerce.api.dtos.order.OrderRequestDto;
import com.ecommerce.api.enums.OrderStatus;
import com.ecommerce.api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Usuários comuns só acessam seus próprios pedidos. Admins acessam qualquer um.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso não autorizado.\"}")
                    )),
            @ApiResponse(responseCode = "403", description = "Acesso negado a este pedido",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso negado a este pedido\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Pedido não encontrado com o ID fornecido\"}")
                    ))
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<FullOrderResponseDto> getOrderById(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

    @Operation(summary = "Retorna os pedidos do usuário logado ou todos os pedidos se for Admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso não autorizado.\"}")
                    )),
    })
    @GetMapping
    public ResponseEntity<List<FullOrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @Operation(summary = "Transforma os itens do carrinho em um pedido e baixa o estoque dos produtos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Carrinho vazio ou estoque insuficiente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"O carrinho está vazio\" }"))
            ),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso não autorizado.\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Carrinho não encontrado para realizar pedido.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Pedido não encontrado com o ID fornecido\"}")
                    ))
    })
    @PostMapping
    public ResponseEntity<FullOrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequestDto));
    }

    @Operation(summary = "Atualiza o status de um pedido : Somente Admins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso não autorizado.\"}")
                    )),
            @ApiResponse(responseCode = "403", description = "Acesso negado: exige privilégios de administrador",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso negado: exige privilégios de administrador\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Pedido não encontrado.\"}")
                    )),
    })
    @PatchMapping("/status/{orderId}")
    public ResponseEntity<FullOrderResponseDto> updateStatus(
            @PathVariable UUID orderId,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }
}