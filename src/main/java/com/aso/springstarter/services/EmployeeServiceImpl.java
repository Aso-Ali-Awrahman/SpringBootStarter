package com.aso.springstarter.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.employee.CreateEmployeeRequest;
import com.aso.springstarter.dtos.employee.EmployeeResponse;
import com.aso.springstarter.dtos.employee.UpdateEmployeePassword;
import com.aso.springstarter.dtos.employee.UpdateEmployeeRequest;
import com.aso.springstarter.entiies.EmployeeEntity;
import com.aso.springstarter.repositories.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
            .stream().map(EmployeeEntity::toDto)
            .toList();
    }

    @Override
    public EmployeeResponse getEmployee(UUID id) {
        return employeeRepository.findById(id)
            .map(EmployeeEntity::toDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    @Override
    @Transactional
    public void createEmployee(CreateEmployeeRequest request) {
        employeeRepository.save(new EmployeeEntity(
            null,
            request.getFullName(),
            request.getEmail(),
            request.getPassword(),
            request.getRole(),
            request.getGender(),
            Instant.now()
        ));
    }

    @Override
    public void updateEmployee(UUID id, UpdateEmployeeRequest request) {
        employeeRepository.findById(id)
            .map(employee -> {
                employee.setFullName(request.getFullName());
                employee.setEmail(request.getEmail());
                employee.setRole(request.getRole());

                return employeeRepository.save(employee);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id) {
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void resetPassword(UUID id, UpdateEmployeePassword request) {
        employeeRepository.findById(id)
            .map(employee -> {
                employee.setPassword(request.getNewPassword());
                return employeeRepository.save(employee);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

}
