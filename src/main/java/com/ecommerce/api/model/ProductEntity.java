package com.ecommerce.api.model;

import com.ecommerce.api.dtos.ProductRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "product_image", nullable = false)
    private String product_image;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateStock(Long quantityChange) {
        if (this.quantity + quantityChange < 0) {
            throw new RestClientException("Estoque insuficiente para o produto: " + this.name);
        }
        this.quantity += quantityChange;
    }

    public void updateDetails(ProductRequestDto dto) {
        if (dto.name() != null) this.name = dto.name();
        if (dto.price() != null) this.price = dto.price();
        if (dto.category() != null) this.category = dto.category();
        if (dto.quantity() != null) this.quantity = dto.quantity();
        if (dto.description() != null) this.description = dto.description();
    }
}