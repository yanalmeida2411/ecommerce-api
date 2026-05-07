package com.ecommerce.api.dtos.order;

import com.ecommerce.api.enums.OrderStatus;
import com.ecommerce.api.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record FullOrderResponseDto(
        UUID orderId,
        List<OrderResponseDto> items,
        BigDecimal totalAmount,
        OrderStatus status,
        PaymentMethod paymentMethod,
        LocalDateTime createdAt
) {
}
