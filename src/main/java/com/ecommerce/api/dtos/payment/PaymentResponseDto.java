package com.ecommerce.api.dtos.payment;

import com.ecommerce.api.dtos.order.FullOrderResponseDto;
import com.ecommerce.api.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponseDto(
        UUID paymentId,
        FullOrderResponseDto order,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt
) {
}