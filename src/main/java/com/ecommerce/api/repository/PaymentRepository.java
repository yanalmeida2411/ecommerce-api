package com.ecommerce.api.repository;

import com.ecommerce.api.model.PaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentsEntity, UUID> {
    Optional<PaymentsEntity> findByOrderId(UUID orderId);

    List<PaymentsEntity> findAllByOrderUserId(UUID userId);
}