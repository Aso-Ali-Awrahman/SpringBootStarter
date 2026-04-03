package com.aso.springstarter.controllers;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.employee.CreateEmployeeRequest;
import com.aso.springstarter.dtos.employee.EmployeeResponse;
import com.aso.springstarter.dtos.employee.RoleResponse;
import com.aso.springstarter.dtos.employee.UpdateEmployeePassword;
import com.aso.springstarter.dtos.employee.UpdateEmployeeRequest;
import com.aso.springstarter.entiies.UserRole;
import com.aso.springstarter.security.authorization.AdminAndAssistantRolesRequired;
import com.aso.springstarter.security.authorization.AdminRoleRequired;
import com.aso.springstarter.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Employee Management Controller (Admin)")
@RestController
@AllArgsConstructor
public class EmployeeController {

    @GetMapping(value = "protected/employees")
    @Operation(summary = "Get all employees")
    @ResponseStatus(HttpStatus.OK)
    @AdminAndAssistantRolesRequired
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    private final EmployeeService employeeService;

    @GetMapping(value = "protected/employees/{id}")
    @Operation(summary = "Get employee by id")
    @ResponseStatus(HttpStatus.OK)
    @AdminAndAssistantRolesRequired
    public EmployeeResponse getEmployee(@PathVariable UUID id) {
        return employeeService.getEmployee(id);
    }

    @PostMapping(value = "protected/employees")
    @Operation(summary = "Create employee")
    @ResponseStatus(HttpStatus.CREATED)
    @AdminRoleRequired
    public void createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        employeeService.createEmployee(request);
    }

    @PutMapping("protected/employees/{id}")
    @Operation(summary = "Update employee")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AdminRoleRequired
    public void updateEmployee(@Valid @RequestBody UpdateEmployeeRequest request, @PathVariable UUID id) {
        employeeService.updateEmployee(id, request);
    }

    @GetMapping("protected/employees/roles")
    @Operation(summary = "Get backoffice user roles")
    @ResponseStatus(HttpStatus.OK)
    @AdminAndAssistantRolesRequired
    public List<RoleResponse> getRoles() {
        return UserRole.getRoles()
            .stream().map(RoleResponse::new).toList();
    }

    @DeleteMapping(value = "protected/employees/{id}")
    @Operation(summary = "Delete employee by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AdminRoleRequired
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }

    @PatchMapping("protected/employees/{id}/reset-password")
    @Operation(summary = "Force reset password of employee")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AdminRoleRequired
    public void resetPassword(@PathVariable UUID id, @Valid @RequestBody UpdateEmployeePassword request) {
        employeeService.resetPassword(id, request);
    }

    @PatchMapping("protected/employees/{id}/activate")
    @Operation(summary = "Activate employee")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AdminRoleRequired
    public void activateEmployee(@PathVariable UUID id) {
        employeeService.activateEmployee(id);
    }

    @PatchMapping("protected/employees/{id}/deactivate")
    @Operation(summary = "Deactivate employee")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AdminRoleRequired
    public void deactivateEmployee(@PathVariable UUID id) {
        employeeService.deactivateEmployee(id);
    }

}
