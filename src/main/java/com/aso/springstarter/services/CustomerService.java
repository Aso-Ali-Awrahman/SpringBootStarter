package com.aso.springstarter.services;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.auth.RegisterCustomerRequest;
import com.aso.springstarter.dtos.customer.CustomerResponse;
import com.aso.springstarter.entiies.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    List<CustomerResponse> getAllCustomers();

    Page<CustomerResponse> getPaginatedCustomers(Pageable pageable, List<UserStatus> statuses);

    CustomerResponse getCustomer(UUID id);

    CustomerResponse getCustomerByEmail(String email);

    CustomerResponse getCustomerByPhoneNumber(String phoneNumber);

    void createCustomer(RegisterCustomerRequest request);

    void activateCustomer(UUID id);

    void deactivateCustomer(UUID id);

}
