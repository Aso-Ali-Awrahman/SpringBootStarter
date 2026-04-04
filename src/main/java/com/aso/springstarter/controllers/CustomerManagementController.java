package com.aso.springstarter.controllers;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.PagingRequest;
import com.aso.springstarter.dtos.customer.CustomerResponse;
import com.aso.springstarter.entiies.UserStatus;
import com.aso.springstarter.security.authorization.AdminAndAssistantRolesRequired;
import com.aso.springstarter.security.authorization.AdminRoleRequired;
import com.aso.springstarter.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Management Controller (Admin)")
@RestController
@AllArgsConstructor
public class CustomerManagementController {

    private final CustomerService customerService;

    @GetMapping(value = "protected/customers/all")
    @Operation(summary = "Get all customers")
    @ResponseStatus(HttpStatus.OK)
    @AdminAndAssistantRolesRequired
    public List<CustomerResponse> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping(value = "protected/customers")
    @Operation(summary = "Get paginated customers")
    @ResponseStatus(HttpStatus.OK)
    @AdminAndAssistantRolesRequired
    public Page<CustomerResponse> getPaginatedCustomers(@RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                        @RequestParam(name = "sort", defaultValue = "id") String sortBy,
                                                        @RequestParam(name = "direction", defaultValue = "asc") String direction,
                                                        @RequestParam(name = "status", defaultValue = "ACTIVE") List<UserStatus> statuses){
        final var allowedSorts = List.of("id", "first_name", "last_name", "email");
        final var pagingRequest = new PagingRequest(page, size, sortBy, direction);
        return customerService.getPaginatedCustomers(pagingRequest.toPageable(allowedSorts), statuses);
    }



    @GetMapping(value = "/protected/customers/{customerId}")
    @Operation(summary = "Get customer by id")
    @ResponseStatus(HttpStatus.OK)
    @AdminAndAssistantRolesRequired
    public CustomerResponse getCustomer(@PathVariable UUID customerId) {
        return customerService.getCustomer(customerId);
    }

    @GetMapping(value = "/protected/customers/email/{email}")
    @Operation(summary = "Get customer by email")
    @ResponseStatus(HttpStatus.OK)
    @AdminAndAssistantRolesRequired
    public CustomerResponse getCustomerByEmail(@PathVariable String email) {
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping(value = "/protected/customers/phone/{phoneNumber}")
    @Operation(summary = "Get customer by phone number")
    @ResponseStatus(HttpStatus.OK)
    @AdminAndAssistantRolesRequired
    public CustomerResponse getCustomerByPhoneNumber(@PathVariable String phoneNumber) {
        return customerService.getCustomerByPhoneNumber(phoneNumber);
    }

    @PatchMapping("protected/customers/{customerId}/activate")
    @Operation(summary = "Activate customer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AdminRoleRequired
    public void activateCustomer(@PathVariable UUID customerId) {
        customerService.activateCustomer(customerId);
    }

    @PatchMapping("protected/customers/{customerId}/deactivate")
    @Operation(summary = "Deactivate customer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AdminRoleRequired
    public void deactivateCustomer(@PathVariable UUID customerId) {
        customerService.deactivateCustomer(customerId);
    }

}
