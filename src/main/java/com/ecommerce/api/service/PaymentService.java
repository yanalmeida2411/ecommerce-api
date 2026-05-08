package com.ecommerce.api.service;

import com.ecommerce.api.dtos.payment.PaymentResponseDto;
import com.ecommerce.api.enums.PaymentStatus;
import com.ecommerce.api.enums.UserRole;
import com.ecommerce.api.mapper.PaymentMapper;
import com.ecommerce.api.model.OrdersEntity;
import com.ecommerce.api.model.PaymentsEntity;
import com.ecommerce.api.model.UserEntity;
import com.ecommerce.api.repository.PaymentRepository;
import com.ecommerce.api.utils.GetAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final GetAuthenticatedUser getAuthenticatedUser;



    @Transactional
    public List<PaymentResponseDto> getAllPayments() {
        UserEntity user = getAuthenticatedUser.getAuthenticatedUser();

        List<PaymentsEntity> orders = (user.getRole() == UserRole.ADMIN)
                ? paymentRepository.findAll()
                : paymentRepository.findAllByOrderUserId(user.getId());

        return orders.stream().map(paymentMapper::toResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(UUID id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toResponseDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagamento não encontrado"));
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByOrderId(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(paymentMapper::toResponseDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagamento para a ordem " + orderId + " não encontrado"));
    }

    @Transactional
    public PaymentResponseDto updatePaymentStatus(UUID paymentId, PaymentStatus newPaymentStatus) {
        PaymentsEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagamento não encontrado"));

        payment.updateStatus(newPaymentStatus);

        OrdersEntity order = payment.getOrder();

        switch (newPaymentStatus) {
            case PAID -> {
                order.markAsProcessing();
            }
            case FAILED, CANCELED -> {
                order.cancelOrder();
                order.getItems().forEach(item -> {
                    item.getProduct().addStock(item.getQuantity());
                });
            }
            case REFUNDED -> {
                order.cancelOrder();
            }
            default -> { /* PENDING não muda o status da ordem */ }
        }

        return paymentMapper.toResponseDto(payment);
    }

    // a lógica de processamento
}