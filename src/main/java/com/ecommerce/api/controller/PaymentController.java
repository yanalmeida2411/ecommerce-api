package com.ecommerce.api.controller;

import com.ecommerce.api.dtos.payment.PaymentResponseDto;
import com.ecommerce.api.enums.PaymentStatus;
import com.ecommerce.api.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Busca todos os pagamentos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamentos encontrados."),
    })
    @GetMapping()
    public ResponseEntity<List<PaymentResponseDto>> getAllPayment() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @Operation(summary = "Busca pagamento pelo id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento encontrado."),
            @ApiResponse(responseCode = "404", description = "Pagamento inexsitente.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Pagamento inexistente.\"}")
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @Operation(summary = "Busca pagamento pelo id do pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento encontrado."),
            @ApiResponse(responseCode = "404", description = "Pagamento inexsitente.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Pagamento inexistente.\"}")
                    )
            )
    })
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByOrderId(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @Operation(summary = "Atualiza um pagamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do Pagamento atualizado."),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso não autorizado.\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Pagamento não existe para atualização.\"}")
                    )
            )
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponseDto> updateStatus(
            @PathVariable UUID id,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, status));
    }

    @Operation(summary = "Cria um pagamento simulado e atualiza o status do pedido e do pagamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento efetuado."),
            @ApiResponse(responseCode = "400", description = "Erro no pagamento.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Erro no pagamento.\"}")
                    )),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Acesso não autorizado.\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Pagamento não existe para atualização.\"}")
                    )
            )
    })
    @PostMapping("/{id}/simular")
    public ResponseEntity<PaymentResponseDto> simularPagamento(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.processarPagamentoSimulado(id));
    }
}