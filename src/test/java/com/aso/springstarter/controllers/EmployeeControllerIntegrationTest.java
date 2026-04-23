package com.aso.springstarter.controllers;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aso.springstarter.IntegrationTestBase;
import com.aso.springstarter.dtos.employee.CreateEmployeeRequest;
import com.aso.springstarter.dtos.employee.EmployeeResponse;
import com.aso.springstarter.dtos.employee.UpdateEmployeePassword;
import com.aso.springstarter.dtos.employee.UpdateEmployeeRequest;
import com.aso.springstarter.entiies.UserRole;
import com.aso.springstarter.entiies.Gender;
import com.aso.springstarter.entiies.UserStatus;
import com.aso.springstarter.services.EmployeeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
public class EmployeeControllerIntegrationTest extends IntegrationTestBase {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private static final String FULL_NAME = "Aso Ali";
    private static final String EMAIL = "aso@mail.com";
    private static final String PASSWORD = "123456788";
    private static final String PHONE_NUMBER = "07501234567";

    @Test
    void shouldGetAllEmployees(SoftAssertions softly) throws Exception {
        // given
        final var id = createEmployee();

        // when
        final var result = mockMvc.perform(get("/protected/employees"))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var rootNode = objectMapper.readTree(result.getResponse().getContentAsString());
        final var response = objectMapper.readValue(rootNode.get("content").toString(), new TypeReference<List<EmployeeResponse>>() {});
        softly.assertThat(response).hasSize(1);
        softly.assertThat(response.getFirst().getId()).isEqualTo(id);
        softly.assertThat(response.getFirst().getFullName()).isEqualTo(FULL_NAME);
        softly.assertThat(response.getFirst().getEmail()).isEqualTo(EMAIL);
        softly.assertThat(response.getFirst().getPhoneNumber()).isEqualTo(PHONE_NUMBER);
        softly.assertThat(response.getFirst().getRole()).isEqualTo(UserRole.ADMIN);
        softly.assertThat(response.getFirst().getGender()).isEqualTo(Gender.MALE);
        softly.assertThat(response.getFirst().getCreatedAt()).isNotNull();
        softly.assertThat(response.getFirst().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void shouldGetProductById(SoftAssertions softly) throws Exception {
        // given
        final var id = createEmployee();

        // when
        final var result = mockMvc.perform(get("/protected/employees/{employeeId}", id))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeResponse.class);
        softly.assertThat(response.getId()).isEqualTo(id);
        softly.assertThat(response.getFullName()).isEqualTo(FULL_NAME);
        softly.assertThat(response.getEmail()).isEqualTo(EMAIL);
        softly.assertThat(response.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
        softly.assertThat(response.getRole()).isEqualTo(UserRole.ADMIN);
        softly.assertThat(response.getGender()).isEqualTo(Gender.MALE);
        softly.assertThat(response.getCreatedAt()).isNotNull();
        softly.assertThat(response.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void shouldReturnErrorWhenEmployeeNotFound(SoftAssertions softly) throws Exception {
        // given
        final var id = UUID.randomUUID();

        // when
        final var result = mockMvc.perform(get("/protected/employees/{employeeId}", id))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getStatus()).isEqualTo(404);
        softly.assertThat(response.getDetail()).isEqualTo("Employee not found");
    }

    @Test
    void shouldCreateEmployee(SoftAssertions softly) throws Exception {
        // given
        final var request = new CreateEmployeeRequest(FULL_NAME, EMAIL, PHONE_NUMBER, "12345678", UserRole.ADMIN, Gender.MALE);

        // when
        mockMvc.perform(
            post("/protected/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated());

        // then
        final var result = mockMvc.perform(get("/protected/employees"))
            .andExpect(status().isOk())
            .andReturn();
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<EmployeeResponse>>() {});
        softly.assertThat(response).hasSize(1);
        softly.assertThat(response.getFirst().getFullName()).isEqualTo(request.getFullName());
        softly.assertThat(response.getFirst().getEmail()).isEqualTo(request.getEmail());
        softly.assertThat(response.getFirst().getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        softly.assertThat(response.getFirst().getRole()).isEqualTo(request.getRole());
        softly.assertThat(response.getFirst().getGender()).isEqualTo(request.getGender());
        softly.assertThat(response.getFirst().getCreatedAt()).isNotNull();
        softly.assertThat(response.getFirst().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void shouldReturnErrorWhenCreatingEmployee(SoftAssertions softly) throws Exception {
        // given
        final var request = new CreateEmployeeRequest("Aso", "mail.com", "", "", null, Gender.MALE);

        // when
        final var result = mockMvc.perform(
            post("/protected/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest()).andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getStatus()).isEqualTo(400);
        softly.assertThat(response.getDetail()).isEqualTo("Validation failed for one or more fields");
        Assertions.assertNotNull(response.getProperties());
        softly.assertThat(response.getProperties().get("errors")).isNotNull();
    }

    @Test
    void shouldUpdateEmployee(SoftAssertions softly) throws Exception {
        // given
        final var id = createEmployee();
        final var request = new UpdateEmployeeRequest("Updated Name", EMAIL, UserRole.DATA_ENTRY);

        // when
        mockMvc.perform(
            put("/protected/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNoContent());

        // then
        final var updatedEmployee = employeeService.getEmployee(id);
        softly.assertThat(updatedEmployee.getFullName()).isEqualTo(request.getFullName());
        softly.assertThat(updatedEmployee.getEmail()).isEqualTo(request.getEmail());
        softly.assertThat(updatedEmployee.getRole()).isEqualTo(request.getRole());
    }

    @Test
    void shouldNotUpdateEmployeeDueNotFound(SoftAssertions softly) throws Exception {
        // given
        final var id = UUID.randomUUID();
        final var request = new UpdateEmployeeRequest("Updated Name", EMAIL, UserRole.DATA_ENTRY);

        // when
        final var result = mockMvc.perform(
            put("/protected/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNotFound()).andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getStatus()).isEqualTo(404);
        softly.assertThat(response.getDetail()).isEqualTo("Employee not found");
    }

    @Test
    void shouldResetEmployeePassword(SoftAssertions softly) throws Exception {
        // given
        final var id = createEmployee();
        final var request = new UpdateEmployeePassword("new password");

        // when & then
        mockMvc.perform(patch("/protected/employees/{id}/reset-password", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteEmployee(SoftAssertions softly) throws Exception {
        // given
        final var id = createEmployee();

        // when & then
        mockMvc.perform(delete("/protected/employees/{id}", id))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldActivateEmployeeById(SoftAssertions softly) throws Exception {
        // given
        final var id = createEmployee();

        // when
        final var result = mockMvc.perform(patch("/protected/employees/{id}/activate", id))
            .andExpect(status().isNoContent())
            .andReturn();

        // then
        final var employee = employeeService.getEmployee(id);
        softly.assertThat(employee.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void shouldNotActivateEmployeeDueNotFound(SoftAssertions softly) throws Exception {
        // when
        final var result = mockMvc.perform(patch("/protected/employees/{id}/activate", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getStatus()).isEqualTo(404);
        softly.assertThat(response.getDetail()).isEqualTo("Employee not found");
    }

    @Test
    void shouldDeactivateEmployeeById(SoftAssertions softly) throws Exception {
        // given
        final var id = createEmployee();

        // when
        final var result = mockMvc.perform(patch("/protected/employees/{id}/deactivate", id))
            .andExpect(status().isNoContent())
            .andReturn();

        // then
        final var employee = employeeService.getEmployee(id);
        softly.assertThat(employee.getStatus()).isEqualTo(UserStatus.BLOCKED);
    }

    @Test
    void shouldNotDeactivateEmployeeDueNotFound(SoftAssertions softly) throws Exception {
        // when
        final var result = mockMvc.perform(patch("/protected/employees/{id}/deactivate", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getStatus()).isEqualTo(404);
        softly.assertThat(response.getDetail()).isEqualTo("Employee not found");
    }

    private UUID createEmployee() {
        employeeService.createEmployee(new CreateEmployeeRequest(
            FULL_NAME, EMAIL, PHONE_NUMBER, PASSWORD, UserRole.ADMIN, Gender.MALE
        ));
        return employeeService.getAllEmployees().getFirst().getId();
    }

}
