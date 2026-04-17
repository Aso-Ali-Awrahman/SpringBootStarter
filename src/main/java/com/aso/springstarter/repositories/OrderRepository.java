package com.aso.springstarter.repositories;

import java.util.UUID;

import com.aso.springstarter.entiies.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    Page<OrderEntity> findAllByCustomerId(Pageable pageable, UUID userId);

    OrderEntity findByIdAndCustomerId(UUID id, UUID userId);

}
