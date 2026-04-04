package com.aso.springstarter.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.aso.springstarter.entiies.EmployeeEntity;
import com.aso.springstarter.entiies.UserRole;
import com.aso.springstarter.entiies.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {

    Optional<EmployeeEntity> findByEmail(String email);

    Page<EmployeeEntity> findAllByStatusInAndRoleIn(Pageable pageable, List<UserStatus> statuses, List<UserRole> roles);

}
