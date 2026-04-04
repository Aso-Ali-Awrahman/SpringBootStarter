package com.aso.springstarter.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.auth.RegisterCustomerRequest;
import com.aso.springstarter.dtos.customer.CustomerResponse;
import com.aso.springstarter.entiies.CustomerEntity;
import com.aso.springstarter.entiies.UserStatus;
import com.aso.springstarter.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
            .map(CustomerEntity::toDto)
            .toList();
    }

    @Override
    public Page<CustomerResponse> getPaginatedCustomers(Pageable pageable, List<UserStatus> statuses) {
        return customerRepository.findAllByStatusIn(pageable, statuses).map(CustomerEntity::toDto);
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
    @Transactional
    public void createCustomer(RegisterCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer with this email already exists");
        }
        customerRepository.save(new CustomerEntity(
            null,
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            UserStatus.ACTIVE,
            request.getPhoneNumber(),
            request.getGender(),
            Instant.now()
        ));
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
