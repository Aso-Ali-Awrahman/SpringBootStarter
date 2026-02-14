package com.aso.springstarter.services;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.customer.CustomerResponse;
import lombok.AllArgsConstructor;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
public class CustomerServiceTest {

    // I won't write the tests ;)...

    @AllArgsConstructor
    private final static class TestCustomerService implements CustomerService {

        @Override
        public List<CustomerResponse> getAllCustomers() {
            return List.of();
        }

        @Override
        public CustomerResponse getCustomer(UUID id) {
            return null;
        }

        @Override
        public CustomerResponse getCustomerByEmail(String email) {
            return null;
        }

        @Override
        public CustomerResponse getCustomerByPhoneNumber(String phoneNumber) {
            return null;
        }

        @Override
        public void activateCustomer(UUID id) {

        }

        @Override
        public void deactivateCustomer(UUID id) {

        }
    }

}
