package com.ecommerce.api.service;

import com.ecommerce.api.dtos.product.ProductRequestDto;
import com.ecommerce.api.dtos.product.ProductResponseDto;
import com.ecommerce.api.mapper.ProductMapper;
import com.ecommerce.api.model.ProductEntity;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.utils.GetAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final GetAuthenticatedUser getAuthenticatedUser;

    public List<ProductResponseDto> findAllProduct() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    public ProductResponseDto findProductById(UUID productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado")
        );

        return productMapper.toResponseDto(product);
    }

    public List<ProductResponseDto> findProductByCategory(String category) {
        List<ProductEntity> products = productRepository.findByCategory(category);

        if (products.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria inexistente");
        }

        return products.stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public ProductResponseDto createProduct(@NonNull ProductRequestDto productDto) {
        getAuthenticatedUser.validateAdminRole();
        if (productRepository.findByName(productDto.name()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto já cadastrado.");
        }

        ProductEntity newProduct = ProductEntity.builder()
                .name(productDto.name())
                .price(productDto.price())
                .category(productDto.category())
                .quantity(productDto.quantity())
                .product_image(productDto.product_image())
                .description(productDto.description())
                .build();

        ProductEntity savedProduct = productRepository.save(newProduct);


        return productMapper.toResponseDto(savedProduct);
    }

    @Transactional
    public ProductResponseDto updatingProduct(UUID productId, ProductRequestDto dto) {
        getAuthenticatedUser.validateAdminRole();
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        if (dto.name() != null && !dto.name().equals(product.getName())) {
            productRepository.findByName(dto.name())
                    .ifPresent(p -> {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome já em uso");
                    });
        }

        product.updateDetails(dto);
        return productMapper.toResponseDto(productRepository.save(product));
    }

    public void deleteProductById(UUID productId) {
        getAuthenticatedUser.validateAdminRole();
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        productRepository.deleteById(productId);
    }

}