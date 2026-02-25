package com.aso.springstarter.repositories;

import java.util.Optional;
import java.util.UUID;

import com.aso.springstarter.entiies.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {

    Optional<EmployeeEntity> findByEmail(String email);

}
