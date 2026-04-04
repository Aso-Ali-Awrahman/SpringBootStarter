package com.aso.springstarter.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.aso.springstarter.entiies.ProductEntity;
import com.aso.springstarter.entiies.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findByIdAndStatus(UUID id, ProductStatus status);

    Page<ProductEntity> findAllByStatusIn(Pageable pageable, List<ProductStatus> statuses);

}
