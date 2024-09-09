package org.amoseman.budgetingwebsitebackend.application.auth;

public class Roles {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    public static boolean valid(String role) {
        return role.equals(USER) || role.equals(ADMIN);
    }
}
