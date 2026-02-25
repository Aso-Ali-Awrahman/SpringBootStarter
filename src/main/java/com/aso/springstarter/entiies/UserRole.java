package com.aso.springstarter.entiies;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
    ADMIN,
    DATA_ENTRY,
    CUSTOMER;

    public static List<UserRole> getRoles() {
        return Arrays.asList(ADMIN, DATA_ENTRY);
    }
}
