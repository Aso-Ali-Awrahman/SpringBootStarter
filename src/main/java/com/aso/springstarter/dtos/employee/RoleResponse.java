package com.aso.springstarter.dtos.employee;

import com.aso.springstarter.entiies.BackofficeUserRole;
import lombok.Value;

@Value
public class RoleResponse {
    BackofficeUserRole role;
}
