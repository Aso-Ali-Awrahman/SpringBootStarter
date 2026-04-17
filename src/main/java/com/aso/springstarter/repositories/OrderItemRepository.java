package com.aso.springstarter.repositories;

import java.util.Optional;
import java.util.UUID;

import com.aso.springstarter.entiies.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {

    Optional<OrderItemEntity> findByIdAndOrderId(UUID id, UUID orderId);

}
