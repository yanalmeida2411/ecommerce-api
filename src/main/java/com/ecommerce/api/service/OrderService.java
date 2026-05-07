package com.ecommerce.api.service;

import com.ecommerce.api.dtos.order.FullOrderResponseDto;
import com.ecommerce.api.dtos.order.OrderRequestDto;
import com.ecommerce.api.enums.OrderStatus;
import com.ecommerce.api.enums.UserRole;
import com.ecommerce.api.mapper.OrderMapper;
import com.ecommerce.api.model.*;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.OrderRepository;
import com.ecommerce.api.utils.GetAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final GetAuthenticatedUser getAuthenticatedUser;
    private final CartRepository cartRepository;

    @Transactional
    public FullOrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        UserEntity user = getAuthenticatedUser.getAuthenticatedUser();

        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrinho não encontrado"));

        if (cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O carrinho está vazio");
        }

        OrdersEntity order = OrdersEntity.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .total_amount(calculateTotal(cart))
                .build();

        List<OrderItemEntity> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    ProductEntity product = cartItem.getProduct();
                    product.subtractStock(cartItem.getQuantity());

                    return OrderItemEntity.builder()
                            .order(order)
                            .product(product)
                            .quantity(cartItem.getQuantity())
                            .priceAtPurchase(product.getPrice())
                            .build();
                }).toList();

        order.assignItems(orderItems);

        PaymentsEntity payment = PaymentsEntity.builder()
                .order(order)
                .status(OrderStatus.PENDING)
                .payment_method(orderRequestDto.paymentMethod())
                .total_amount(order.getTotal_amount())
                .build();

        order.assignPayment(payment);

        OrdersEntity savedOrder = orderRepository.save(order);

        cartRepository.delete(cart);

        return orderMapper.toResponseDto(savedOrder);
    }

    private BigDecimal calculateTotal(CartEntity cart) {
        return cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public FullOrderResponseDto findOrderById(UUID orderId) {
        UserEntity user = getAuthenticatedUser.getAuthenticatedUser();

        OrdersEntity order;
        if (user.getRole() == UserRole.ADMIN) {
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        } else {
            order = orderRepository.findByIdAndUserId(orderId, user.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a este pedido"));
        }

        return orderMapper.toResponseDto(order);
    }

    @Transactional
    public FullOrderResponseDto updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        getAuthenticatedUser.validateAdminRole();

        OrdersEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

        order.updateStatus(newStatus);
        orderRepository.save(order);

        return orderMapper.toResponseDto(order);
    }

    @Transactional(readOnly = true)
    public List<FullOrderResponseDto> findAllOrders() {
        UserEntity user = getAuthenticatedUser.getAuthenticatedUser();

        List<OrdersEntity> orders = (user.getRole() == UserRole.ADMIN)
                ? orderRepository.findAll()
                : orderRepository.findAllByUserId(user.getId());

        return orders.stream().map(orderMapper::toResponseDto).toList();
    }
}