package com.ecommerce.api.service;

import com.ecommerce.api.dtos.cart.CartRequestDto;
import com.ecommerce.api.dtos.cart.CartUpdateQuantityDto;
import com.ecommerce.api.dtos.cart.FullCartResponseDto;
import com.ecommerce.api.mapper.CartMapper;
import com.ecommerce.api.model.CartEntity;
import com.ecommerce.api.model.CartItemEntity;
import com.ecommerce.api.model.ProductEntity;
import com.ecommerce.api.model.UserEntity;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.UserRepository;
import com.ecommerce.api.utils.GetAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final GetAuthenticatedUser getAuthenticatedUser;

    @Transactional(readOnly = true)
    public FullCartResponseDto findCartTotalByUser() {
        UserEntity user = getAuthenticatedUser.getAuthenticatedUser();
        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, "Carrinho vazio"));
        return cartMapper.toFullResponseDto(cart);
    }

    @Transactional
    public FullCartResponseDto addToCart(CartRequestDto dto) {
        if (dto.quantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade deve ser maior que zero.");
        }
        UserEntity currentUser = getAuthenticatedUser.getAuthenticatedUser();

        CartEntity cart = cartRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> cartRepository.save(CartEntity.builder().user(currentUser).items(new ArrayList<>()).build()));

        Optional<CartItemEntity> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(dto.productId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + dto.quantity());
        } else {
            ProductEntity product = productRepository.findById(dto.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto inexistente"));

            cart.getItems().add(CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(dto.quantity())
                    .priceAtPurchase(product.getPrice())
                    .build());
        }

        return cartMapper.toFullResponseDto(cartRepository.save(cart));
    }

    @Transactional
    public FullCartResponseDto updateCartQuantity(CartUpdateQuantityDto dto) {
        if (dto.quantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade deve ser maior que zero.");
        }
        UserEntity currentUser = getAuthenticatedUser.getAuthenticatedUser();

        CartEntity cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrinho não encontrado"));

        CartItemEntity item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(dto.productId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado no carrinho"));

        item.setQuantity(dto.quantity());

        return cartMapper.toFullResponseDto(cartRepository.save(cart));
    }

    @Transactional
    public void removeProductCompletely(UUID productId) {
        UserEntity user = getAuthenticatedUser.getAuthenticatedUser();
        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrinho não encontrado"));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }

    @Transactional
    public void clearFullCart() {
        UserEntity user = getAuthenticatedUser.getAuthenticatedUser();
        cartRepository.deleteByUserId(user.getId());
    }
}