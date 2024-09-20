package org.amoseman.budgetingbackend.application.auth;

import java.util.Set;

/**
 * Provides utility methods for role parsing and processing.
 */
public class Roles {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    /**
     * Check if a role is valid.
     * @param role the role.
     * @return true if it is valid, false if otherwise.
     */
    public static boolean valid(String role) {
        return role.equals(USER) || role.equals(ADMIN);
    }

    /**
     * Convert a set of roles as a comma-delimited string.
     * @param roles the set of roles.
     * @return the roles as a string.
     */
    public static String asString(Set<String> roles) {
        String[] array = roles.toArray(new String[0]);
        if (0 == array.length) {
            return "";
        }
        if (1 == array.length) {
            return array[0];
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length - 1; i++) {
            builder.append(array[i]).append(',');
        }
        builder.append(array[array.length - 1]);
        return builder.toString();
    }

    /**
     * Convert a comma-delimited string of roles into a set of roles.
     * @param roles the roles.
     * @return the set of roles.
     */
    public static Set<String> fromString(String roles) {
        return Set.of(roles.split(","));
    }
}
