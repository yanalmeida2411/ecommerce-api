package com.ecommerce.api.dtos.order;

import com.ecommerce.api.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderUpdateStatus(
        @Schema(example = "cba5f339-0e3c-47d7-b4f9-fdab5d73014f") UUID orderId,
        @NotNull
        @Schema(example = "Processing")
        OrderStatus orderStatus
) {
}
