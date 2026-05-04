package com.ecommerce.api.mapper;

import com.ecommerce.api.dtos.CartResponseDto;
import com.ecommerce.api.model.CartEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartMapper {
    public CartResponseDto toResponseDto(CartEntity cart) {
        if (cart == null) return null;

        return new CartResponseDto(
                cart.getId(),
                cart.getProductId().getId(),
                cart.getProductId().getName(),
                cart.getPrice(),
                cart.getQuantity(),
                cart.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()))
        );
    }
}
