package com.ecommerce.api.repository;

import com.ecommerce.api.model.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrdersEntity, UUID> {

    List<OrdersEntity> findAllByUserId(UUID userId);

    Optional<OrdersEntity> findByIdAndUserId(UUID orderId, UUID userId);
}
