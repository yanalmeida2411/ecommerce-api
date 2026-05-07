package com.ecommerce.api.dtos.order;

import com.ecommerce.api.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;


public record OrderRequestDto(
        @Schema(example = "PIX")
        PaymentMethod paymentMethod
) {
}
