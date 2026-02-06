package com.aso.springstarter.controllers;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.employee.CreateEmployeeRequest;
import com.aso.springstarter.dtos.employee.EmployeeResponse;
import com.aso.springstarter.dtos.employee.RoleResponse;
import com.aso.springstarter.dtos.employee.UpdateEmployeeRequest;
import com.aso.springstarter.entiies.BackofficeUserRole;
import com.aso.springstarter.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Employee Controller")
@RestController
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(value = "protected/employees")
    @Operation(summary = "Get all employees (Admin)")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping(value = "protected/employees/{id}")
    @Operation(summary = "Get employee by id (Admin)")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse getEmployee(@PathVariable UUID id) {
        return employeeService.getEmployee(id);
    }

    @PostMapping(value = "protected/employees")
    @Operation(summary = "Create employee (Admin)")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        employeeService.createEmployee(request);
    }

    @PutMapping("protected/employees/{id}")
    @Operation(summary = "Update employee (Admin)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmployee(@Valid @RequestBody UpdateEmployeeRequest request, @PathVariable UUID id) {
        employeeService.updateEmployee(id, request);
    }

    @GetMapping("protected/employees/roles")
    @Operation(summary = "Get backoffice user roles (Admin)")
    @ResponseStatus(HttpStatus.OK)
    public List<RoleResponse> getRoles() {
        return BackofficeUserRole.getRoles()
            .stream().map(RoleResponse::new).toList();
    }


}
