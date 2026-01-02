package com.aso.springstarter.repositories;

import java.util.UUID;

import com.aso.springstarter.entiies.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
