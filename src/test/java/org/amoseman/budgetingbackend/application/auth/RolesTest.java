package org.amoseman.budgetingbackend.application.auth;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RolesTest {

    @Test
    void valid() {
        assertTrue(Roles.valid("ADMIN"));
        assertTrue(Roles.valid("USER"));
        assertFalse(Roles.valid("blahblah"));
    }

    @Test
    void asString() {
        String roles = Roles.asString(Set.of("USER", "ADMIN"));
        boolean flag = "USER,ADMIN".equals(roles) || "ADMIN,USER".equals(roles);
        assertTrue(flag);
    }

    @Test
    void fromString() {
        Set<String> roles = Roles.fromString("USER,ADMIN");
        assertTrue(roles.contains("USER"));
        assertTrue(roles.contains("ADMIN"));
    }
}