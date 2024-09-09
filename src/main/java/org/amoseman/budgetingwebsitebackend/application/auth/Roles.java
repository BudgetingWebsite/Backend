package org.amoseman.budgetingwebsitebackend.application.auth;

import java.util.Iterator;
import java.util.Set;

public class Roles {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    public static boolean valid(String role) {
        return role.equals(USER) || role.equals(ADMIN);
    }

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

    public static Set<String> fromString(String roles) {
        return Set.of(roles.split(","));
    }
}
