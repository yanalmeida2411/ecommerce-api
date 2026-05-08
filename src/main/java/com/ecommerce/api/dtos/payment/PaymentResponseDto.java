package com.ecommerce.api.dtos.payment;

import com.ecommerce.api.dtos.order.FullOrderResponseDto;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponseDto(
        UUID paymentId,
        FullOrderResponseDto order,
        LocalDateTime createdAt
) {
}