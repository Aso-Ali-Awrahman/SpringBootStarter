package com.aso.springstarter.services;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.employee.CreateEmployeeRequest;
import com.aso.springstarter.dtos.employee.EmployeeResponse;
import com.aso.springstarter.dtos.employee.UpdateEmployeePassword;
import com.aso.springstarter.dtos.employee.UpdateEmployeeRequest;
import com.aso.springstarter.entiies.UserRole;
import com.aso.springstarter.entiies.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    List<EmployeeResponse> getAllEmployees();

    Page<EmployeeResponse> getPaginatedEmployees(Pageable pageable, List<UserStatus> statuses, List<UserRole> roles);

    EmployeeResponse getEmployee(UUID id);

    void createEmployee(CreateEmployeeRequest request);

    void updateEmployee(UUID id, UpdateEmployeeRequest request);

    void deleteEmployee(UUID id);

    void resetPassword(UUID id, UpdateEmployeePassword request);

    void activateEmployee(UUID id);

    void deactivateEmployee(UUID id);

}
