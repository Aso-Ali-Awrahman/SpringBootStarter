package com.aso.springstarter.controllers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aso.springstarter.IntegrationTestBase;
import com.aso.springstarter.dtos.customer.CustomerResponse;
import com.aso.springstarter.entiies.CustomerEntity;
import com.aso.springstarter.entiies.Gender;
import com.aso.springstarter.entiies.UserStatus;
import com.aso.springstarter.repositories.CustomerRepository;
import com.aso.springstarter.services.CustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ProblemDetail;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
public class CustomerManagementControllerIntegrationTest extends IntegrationTestBase {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;

    private static final String FIRST_NAME = "Aso";
    private static final String LAST_NAME = "Ali";
    private static final String EMAIL = "aso@mail.com";
    private static final String PHONE_NUMBER = "07501234567";
    private static final String PASSWORD = "12345678";


    @Test
    void shouldGetAllCustomers(SoftAssertions softly) throws Exception {
        // given
        final var customer = createCustomer();

        // when
        final var result = mockMvc.perform(get("/protected/customers"))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var rootNode = objectMapper.readTree(result.getResponse().getContentAsString());
        final var customers = objectMapper.readValue(rootNode.get("content").toString(), new TypeReference<List<CustomerResponse>>() {});
        softly.assertThat(customers.getFirst().getId()).isEqualTo(customer.getId());
        softly.assertThat(customers.getFirst().getFirstName()).isEqualTo(customer.getFirstName());
        softly.assertThat(customers.getFirst().getLastName()).isEqualTo(customer.getLastName());
        softly.assertThat(customers.getFirst().getEmail()).isEqualTo(customer.getEmail());
        softly.assertThat(customers.getFirst().getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        softly.assertThat(customers.getFirst().getGender()).isEqualTo(customer.getGender());
        softly.assertThat(customers.getFirst().getStatus()).isEqualTo(customer.getStatus());
        softly.assertThat(customers.getFirst().getCreatedAt()).isEqualTo(customer.getCreatedAt());
    }

    @Test
    void shouldReturnEmptyListWhenNoCustomersFound(SoftAssertions softly) throws Exception {
        // when
        final var result = mockMvc.perform(get("/protected/customers"))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var rootNode = objectMapper.readTree(result.getResponse().getContentAsString());
        final var customers = objectMapper.readValue(rootNode.get("content").toString(), new TypeReference<List<CustomerResponse>>() {});
        softly.assertThat(customers).isEmpty();
    }

    @Test
    void shouldGetCustomerById(SoftAssertions softly) throws Exception {
        // given
        final var customer = createCustomer();

        // when
        final var result = mockMvc.perform(get("/protected/customers/{customerId}", customer.getId()))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerResponse.class);
        softly.assertThat(response.getId()).isEqualTo(customer.getId());
        softly.assertThat(response.getFirstName()).isEqualTo(customer.getFirstName());
        softly.assertThat(response.getLastName()).isEqualTo(customer.getLastName());
        softly.assertThat(response.getEmail()).isEqualTo(customer.getEmail());
        softly.assertThat(response.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        softly.assertThat(response.getGender()).isEqualTo(customer.getGender());
        softly.assertThat(response.getStatus()).isEqualTo(customer.getStatus());
        softly.assertThat(response.getCreatedAt()).isEqualTo(customer.getCreatedAt());
    }

    @Test
    void shouldReturnErrorDueCustomerNotFoundById(SoftAssertions softly) throws Exception {
        // when
        final var result = mockMvc.perform(get("/protected/customers/{customerId}", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getDetail()).isEqualTo("Customer not found");
        softly.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void shouldGetCustomerByEmail(SoftAssertions softly) throws Exception {
        // given
        final var customer = createCustomer();

        // when
        final var result = mockMvc.perform(get("/protected/customers/email/{email}", customer.getEmail()))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerResponse.class);
        softly.assertThat(response.getId()).isEqualTo(customer.getId());
        softly.assertThat(response.getFirstName()).isEqualTo(customer.getFirstName());
        softly.assertThat(response.getLastName()).isEqualTo(customer.getLastName());
        softly.assertThat(response.getEmail()).isEqualTo(customer.getEmail());
        softly.assertThat(response.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        softly.assertThat(response.getGender()).isEqualTo(customer.getGender());
        softly.assertThat(response.getStatus()).isEqualTo(customer.getStatus());
        softly.assertThat(response.getCreatedAt()).isEqualTo(customer.getCreatedAt());
    }

    @Test
    void shouldReturnErrorDueCustomerNotFoundByEmail(SoftAssertions softly) throws Exception {
        // when
        final var result = mockMvc.perform(get("/protected/customers/email/{email}", "aso@mail.com"))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getDetail()).isEqualTo("Customer not found with this email");
        softly.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void shouldGetCustomerByPhoneNumber(SoftAssertions softly) throws Exception {
        // given
        final var customer = createCustomer();

        // when
        final var result = mockMvc.perform(get("/protected/customers/phone/{phoneNumber}", customer.getPhoneNumber()))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), CustomerResponse.class);
        softly.assertThat(response.getId()).isEqualTo(customer.getId());
        softly.assertThat(response.getFirstName()).isEqualTo(customer.getFirstName());
        softly.assertThat(response.getLastName()).isEqualTo(customer.getLastName());
        softly.assertThat(response.getEmail()).isEqualTo(customer.getEmail());
        softly.assertThat(response.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        softly.assertThat(response.getGender()).isEqualTo(customer.getGender());
        softly.assertThat(response.getStatus()).isEqualTo(customer.getStatus());
        softly.assertThat(response.getCreatedAt()).isEqualTo(customer.getCreatedAt());
    }

    @Test
    void shouldReturnErrorDueCustomerNotFoundByPhoneNumber(SoftAssertions softly) throws Exception {
        // when
        final var result = mockMvc.perform(get("/protected/customers/phone/{phoneNumber}", "07501122334"))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getDetail()).isEqualTo("Customer not found with this phone number");
        softly.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void shouldActivateCustomerById(SoftAssertions softly) throws Exception {
        // given
        final var customer = createCustomer();

        // when
        final var result = mockMvc.perform(patch("/protected/customers/{customerId}/activate", customer.getId()))
            .andExpect(status().isNoContent())
            .andReturn();

        // then
        final var updatedCustomer = customerService.getCustomer(customer.getId());
        softly.assertThat(updatedCustomer.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void shouldNotActivateCustomerDueNotFound(SoftAssertions softly) throws Exception {
        // when
        final var result = mockMvc.perform(patch("/protected/customers/{customerId}/activate", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getDetail()).isEqualTo("Customer not found");
        softly.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void shouldDeactivateCustomerById(SoftAssertions softly) throws Exception {
        // given
        final var customer = createCustomer();

        // when
        final var result = mockMvc.perform(patch("/protected/customers/{customerId}/deactivate", customer.getId()))
            .andExpect(status().isNoContent())
            .andReturn();

        // then
        final var updatedCustomer = customerService.getCustomer(customer.getId());
        softly.assertThat(updatedCustomer.getStatus()).isEqualTo(UserStatus.BLOCKED);
    }

    @Test
    void shouldNotDeactivateCustomerDueNotFound(SoftAssertions softly) throws Exception {
        // when
        final var result = mockMvc.perform(patch("/protected/customers/{customerId}/deactivate", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getDetail()).isEqualTo("Customer not found");
        softly.assertThat(response.getStatus()).isEqualTo(404);
    }

    private CustomerResponse createCustomer() {
        return customerRepository.save(new CustomerEntity(
            null, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, UserStatus.ACTIVE, PHONE_NUMBER, Gender.MALE, Instant.now(), null
        )).toDto();
    }


}
