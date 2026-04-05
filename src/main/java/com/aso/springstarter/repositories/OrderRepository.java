package com.aso.springstarter.repositories;

import java.util.UUID;

import com.aso.springstarter.entiies.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

}
