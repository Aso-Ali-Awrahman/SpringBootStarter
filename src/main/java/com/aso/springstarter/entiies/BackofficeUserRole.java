package com.aso.springstarter.entiies;

import java.util.Arrays;
import java.util.List;

public enum BackofficeUserRole {
    ADMIN,
    DATA_ENTRY;

    public static List<BackofficeUserRole> getRoles() {
        return Arrays.asList(ADMIN, DATA_ENTRY);
    }
}
