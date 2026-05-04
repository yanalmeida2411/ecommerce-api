package com.ecommerce.api.service;

import com.ecommerce.api.dtos.CartRequestDto;
import com.ecommerce.api.dtos.CartResponseDto;
import com.ecommerce.api.dtos.CartUpdateQuantityDto;
import com.ecommerce.api.mapper.CartMapper;
import com.ecommerce.api.model.CartEntity;
import com.ecommerce.api.model.ProductEntity;
import com.ecommerce.api.model.UserEntity;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    private UserEntity getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário logado não encontrado"));
    }

    public List<CartResponseDto> findCartsByUser() {
        UserEntity currentUser = getAuthenticatedUser();
        return cartRepository.findByUserId_Id(currentUser.getId()).stream()
                .map(cartMapper::toResponseDto)
                .toList();
    }

    private CartEntity createNewItem(UserEntity user, CartRequestDto dto) {
        ProductEntity product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        return CartEntity.builder()
                .userId(user)
                .productId(product)
                .price(product.getPrice())
                .quantity(dto.quantity())
                .sessionId(user.getId())
                .build();
    }

    @Transactional
    public CartResponseDto addToCart(CartRequestDto dto) {
        UserEntity currentUser = getAuthenticatedUser();

        CartEntity cartItem = cartRepository.findByUserId_IdAndProductId_Id(currentUser.getId(), dto.productId())
                .map(item -> {
                    item.incrementQuantity(dto.quantity());
                    return item;
                })
                .orElseGet(() -> createNewItem(currentUser, dto));

        return cartMapper.toResponseDto(cartRepository.save(cartItem));
    }


    @Transactional
    public CartResponseDto updateCartQuantity(UUID cartId, CartUpdateQuantityDto dto) {
        UserEntity currentUser = getAuthenticatedUser();

        CartEntity cartItem = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item do carrinho não encontrado"));

        if (!cartItem.getUserId().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ação não permitida para este usuário");
        }

        if (!cartItem.getProductId().getId().equals(dto.productId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O produto informado não corresponde ao item do carrinho");
        }

        cartItem.updateQuantity(dto);

        return cartMapper.toResponseDto(cartRepository.save(cartItem));
    }

    @Transactional
    public void removeProductCompletely(UUID productId) {
        UserEntity currentUser = getAuthenticatedUser();

        CartEntity item = cartRepository.findByUserId_IdAndProductId_Id(currentUser.getId(), productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não está no seu carrinho"));

        cartRepository.delete(item);
    }

    @Transactional
    public void clearFullCart() {
        UserEntity currentUser = getAuthenticatedUser();
        cartRepository.deleteByUserId(currentUser.getId());
    }
}
