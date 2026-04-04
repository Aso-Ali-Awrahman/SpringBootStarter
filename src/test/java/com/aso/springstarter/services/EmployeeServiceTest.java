package com.aso.springstarter.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.employee.CreateEmployeeRequest;
import com.aso.springstarter.dtos.employee.EmployeeResponse;
import com.aso.springstarter.dtos.employee.UpdateEmployeePassword;
import com.aso.springstarter.dtos.employee.UpdateEmployeeRequest;
import com.aso.springstarter.entiies.UserRole;
import com.aso.springstarter.entiies.Gender;
import com.aso.springstarter.entiies.UserStatus;
import lombok.AllArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(SoftAssertionsExtension.class)
class EmployeeServiceTest {

    private static final UUID EMPLOYEE_ID_1 = UUID.fromString("80db159b-963f-4071-9b15-9b963f0071e5");
    private static final UUID EMPLOYEE_ID_2 = UUID.fromString("81d2159b-963f-4071-9b15-9b963f0071e6");
    private static final String FULL_NAME = "Aso Ali";
    private static final String EMAIL = "aso@mail.com";
    private static final String PHONE_NUMBER = "07501234567";

    @Test
    void shouldReturnAllEmployees(SoftAssertions softly) {
        // given
        final var employeeService = new TestEmployeeService(false);

        // when
        final var employees = employeeService.getAllEmployees();

        // then
        softly.assertThat(employees).hasSize(2);
        softly.assertThat(employees.getFirst().getId()).isEqualTo(EMPLOYEE_ID_1);
        softly.assertThat(employees.getLast().getId()).isEqualTo(EMPLOYEE_ID_2);
        softly.assertThat(employees.getFirst().getFullName()).isEqualTo(FULL_NAME);
        softly.assertThat(employees.getFirst().getEmail()).isEqualTo(EMAIL);
        softly.assertThat(employees.getFirst().getPhoneNumber()).isEqualTo(PHONE_NUMBER);
        softly.assertThat(employees.getFirst().getRole()).isEqualTo(UserRole.ADMIN);
        softly.assertThat(employees.getFirst().getStatus()).isEqualTo(UserStatus.ACTIVE);
        softly.assertThat(employees.getFirst().getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    void shouldReturnEmployeeById(SoftAssertions softly) {
        // given
        final var employeeService = new TestEmployeeService(false);
        final var id = UUID.randomUUID();

        // when
        final var employee = employeeService.getEmployee(id);

        // then
        softly.assertThat(employee).isNotNull();
        Assertions.assertNotNull(employee);
        softly.assertThat(employee.getId()).isEqualTo(id);
        softly.assertThat(employee.getFullName()).isEqualTo(FULL_NAME);
        softly.assertThat(employee.getEmail()).isEqualTo(EMAIL);
        softly.assertThat(employee.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
        softly.assertThat(employee.getRole()).isEqualTo(UserRole.ADMIN);
        softly.assertThat(employee.getStatus()).isEqualTo(UserStatus.ACTIVE);
        softly.assertThat(employee.getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    void shouldNotReturnEmployeeDueNotFound(SoftAssertions softly) {
        // given
        final var employeeService = new TestEmployeeService(true);
        final var id = UUID.randomUUID();

        // when
        final var employee = employeeService.getEmployee(id);

        // then
        softly.assertThat(employee).isNull();
    }
    
    @Test
    void shouldCreateEmployee(SoftAssertions softly) {
        // given 
        final var employeeService = new TestEmployeeService(false);
        final var request = new CreateEmployeeRequest(FULL_NAME, EMAIL, "07501234567", "1233445676", UserRole.ADMIN, Gender.MALE);
        
        // when
        employeeService.createEmployee(request);
        
        // then
        softly.assertThat(employeeService.count).isEqualTo(1);
    }
    
    @Test
    void shouldUpdateEmployee(SoftAssertions softly) {
        // given 
        final var employeeService = new TestEmployeeService(false);
        final var id = UUID.randomUUID();
        final var request = new UpdateEmployeeRequest(FULL_NAME, EMAIL, UserRole.ADMIN);

        // when
        employeeService.updateEmployee(id, request);

        // then
        softly.assertThat(employeeService.count).isEqualTo(1);
    }

    @Test
    void shouldUpdateEmployeePassword(SoftAssertions softly) {
        // given
        final var employeeService = new TestEmployeeService(false);
        final var id = UUID.randomUUID();
        final var request = new UpdateEmployeePassword("");

        // when
        employeeService.resetPassword(id, request);

        // then
        softly.assertThat(employeeService.count).isEqualTo(1);
    }

    @Test
    void shouldDeleteEmployee(SoftAssertions softly) {
        // given
        final var employeeService = new TestEmployeeService(false);
        final var id = UUID.randomUUID();

        // when
        employeeService.deleteEmployee(id);

        // then
        softly.assertThat(employeeService.count).isEqualTo(1);
    }


    @AllArgsConstructor
    static final class TestEmployeeService implements EmployeeService {
        private final boolean isFailed;
        private int count;
        
        public TestEmployeeService(boolean isFailed) {
            this(isFailed, 0);
        }

        @Override
        public List<EmployeeResponse> getAllEmployees() {
            return List.of(
                new EmployeeResponse(EMPLOYEE_ID_1, FULL_NAME, EMAIL, PHONE_NUMBER, UserRole.ADMIN, UserStatus.ACTIVE, Gender.MALE, Instant.now()),
                new EmployeeResponse(EMPLOYEE_ID_2, "Alice Bob", "alice@mail.com", "07500012121", UserRole.DATA_ENTRY, UserStatus.ACTIVE, Gender.FEMALE, Instant.now())
            );
        }

        @Override
        public Page<EmployeeResponse> getPaginatedEmployees(Pageable pageable, List<UserStatus> statuses, List<UserRole> roles) {
            return null;
        }

        @Override
        public EmployeeResponse getEmployee(UUID id) {
            if (isFailed) {
                return null;
            }
            return new EmployeeResponse(
                id, FULL_NAME, EMAIL, PHONE_NUMBER, UserRole.ADMIN, UserStatus.ACTIVE, Gender.MALE, Instant.now()
            );
        }

        @Override
        public void createEmployee(CreateEmployeeRequest request) {
            if (isFailed) {
                return;
            }
            count++;
        }

        @Override
        public void updateEmployee(UUID id, UpdateEmployeeRequest request) {
            if (isFailed) {
                return;
            }
            count++;
        }

        @Override
        public void deleteEmployee(UUID id) {
            if (isFailed) {
                return;
            }
            count++;
        }

        @Override
        public void resetPassword(UUID id, UpdateEmployeePassword request) {
            if (isFailed) {
                return;
            }
            count++;
        }

        @Override
        public void activateEmployee(UUID id) {

        }

        @Override
        public void deactivateEmployee(UUID id) {

        }
    }

}
