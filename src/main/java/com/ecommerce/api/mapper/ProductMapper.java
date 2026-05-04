package com.ecommerce.api.mapper;

import com.ecommerce.api.dtos.ProductResponseDto;
import com.ecommerce.api.model.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponseDto toResponseDto(ProductEntity product) {
        if (product == null) {
            return null;
        }

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.getQuantity(),
                product.getProduct_image(),
                product.getDescription()
        );
    }
}
