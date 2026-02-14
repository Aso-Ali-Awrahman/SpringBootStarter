package com.aso.springstarter.services;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.customer.CustomerResponse;
import com.aso.springstarter.entiies.CustomerEntity;
import com.aso.springstarter.entiies.UserStatus;
import com.aso.springstarter.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
            .map(CustomerEntity::toDto)
            .toList();
    }

    @Override
    public CustomerResponse getCustomer(UUID id) {
        return customerRepository.findById(id)
            .map(CustomerEntity::toDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    @Override
    public CustomerResponse getCustomerByEmail(String email) {
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with this email");
        }
        return customerRepository.findByEmail(email)
            .map(CustomerEntity::toDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with this email"));
    }

    @Override
    public CustomerResponse getCustomerByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with this phone number");
        }
        return customerRepository.findByPhoneNumber(phoneNumber)
            .map(CustomerEntity::toDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with this phone number"));
    }

    @Override
    public void activateCustomer(UUID id) {
        customerRepository.findById(id)
            .map(customer -> {
                customer.setStatus(UserStatus.ACTIVE);
                return customerRepository.save(customer);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    @Override
    public void deactivateCustomer(UUID id) {
        customerRepository.findById(id)
            .map(customer -> {
                customer.setStatus(UserStatus.BLOCKED);
                return customerRepository.save(customer);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }
}
