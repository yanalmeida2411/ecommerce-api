package com.ecommerce.api.mapper;

import com.ecommerce.api.dtos.CartResponseDto;
import com.ecommerce.api.dtos.FullCartResponseDto;
import com.ecommerce.api.model.CartEntity;
import com.ecommerce.api.model.CartItemEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {
    public CartResponseDto toItemResponseDto(CartItemEntity item) {
        if (item == null) return null;
        return new CartResponseDto(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getPriceAtPurchase(),
                item.getQuantity(),
                item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }

    public FullCartResponseDto toFullResponseDto(CartEntity cart) {
        List<CartResponseDto> items = cart.getItems().stream()
                .map(this::toItemResponseDto)
                .toList();

        BigDecimal total = items.stream()
                .map(CartResponseDto::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new FullCartResponseDto(cart.getUser().getId(), items, total);
    }
}
