package com.ecommerce.api.controller;

import com.ecommerce.api.dtos.payment.PaymentResponseDto;
import com.ecommerce.api.enums.PaymentStatus;
import com.ecommerce.api.service.PaymentService;
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

    @GetMapping()
    public ResponseEntity<List<PaymentResponseDto>> getAllPayment() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByOrderId(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponseDto> updateStatus(
            @PathVariable UUID id,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, status));
    }

    /*
       Abaixo, um exemplo de rota que você precisará se for integrar com gateways.
       Normalmente não enviamos um PUT manual para "pagar", mas sim recebemos um
       aviso (Webhook) do sistema de pagamento.
    */
    /*@PostMapping("/webhook")
    public ResponseEntity<Void> handlePaymentNotification(@RequestBody String payload) {
        // Lógica para processar a notificação e atualizar o status para PAID ou FAILED
        return ResponseEntity.noContent().build();
    }*/
}