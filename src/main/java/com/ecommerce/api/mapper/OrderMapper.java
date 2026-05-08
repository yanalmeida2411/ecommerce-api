package com.ecommerce.api.mapper;

import com.ecommerce.api.dtos.order.FullOrderResponseDto;
import com.ecommerce.api.dtos.order.OrderResponseDto;
import com.ecommerce.api.enums.OrderStatus;
import com.ecommerce.api.model.OrdersEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public FullOrderResponseDto toResponseDto(OrdersEntity order) {
        List<OrderResponseDto> itemDtos = order.getItems().stream()
                .map(item -> new OrderResponseDto(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtPurchase()
                )).toList();

        return new FullOrderResponseDto(
                order.getId(),
                itemDtos,
                order.getTotal_amount(),
                order.getOrderStatus(),
                order.getPayment() != null ? order.getPayment().getPaymentMethod() : null,
                order.getCreatedAt()
        );
    }
}