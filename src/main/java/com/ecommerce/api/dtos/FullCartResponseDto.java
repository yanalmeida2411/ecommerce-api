package com.ecommerce.api.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record FullCartResponseDto(
        UUID userId,
        List<CartResponseDto> items,
        BigDecimal totalCartValue
) {
}
