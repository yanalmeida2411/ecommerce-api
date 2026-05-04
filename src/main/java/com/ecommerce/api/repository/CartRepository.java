package com.ecommerce.api.repository;

import com.ecommerce.api.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    Optional<CartEntity> findByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartEntity c WHERE c.user.id = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}

