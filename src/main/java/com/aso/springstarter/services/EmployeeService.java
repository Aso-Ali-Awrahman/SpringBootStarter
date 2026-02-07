package com.aso.springstarter.services;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.employee.CreateEmployeeRequest;
import com.aso.springstarter.dtos.employee.EmployeeResponse;
import com.aso.springstarter.dtos.employee.UpdateEmployeePassword;
import com.aso.springstarter.dtos.employee.UpdateEmployeeRequest;

public interface EmployeeService {

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse getEmployee(UUID id);

    void createEmployee(CreateEmployeeRequest request);

    void updateEmployee(UUID id, UpdateEmployeeRequest request);

    void deleteEmployee(UUID id);

    void resetPassword(UUID id, UpdateEmployeePassword request);

}
