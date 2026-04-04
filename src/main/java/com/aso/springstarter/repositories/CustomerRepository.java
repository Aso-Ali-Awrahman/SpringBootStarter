package com.aso.springstarter.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.aso.springstarter.entiies.CustomerEntity;
import com.aso.springstarter.entiies.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

    // SELECT * FROM CUSTOMER WHERE EMAIL = ?
    Optional<CustomerEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<CustomerEntity> findByPhoneNumber(String phoneNumber);

    Page<CustomerEntity> findAllByStatusIn(Pageable pageable, List<UserStatus> statuses);
}
