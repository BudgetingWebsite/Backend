package org.amoseman.budgetingwebsitebackend.application.auth;

import org.amoseman.budgetingwebsitebackend.application.auth.hashing.ArgonHasher;
import org.amoseman.budgetingwebsitebackend.application.auth.hashing.Hasher;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

class ArgonHasherTest {

    @Test
    void hash() {
        SecureRandom random = new SecureRandom();
        Hasher hasher = new ArgonHasher(random, 16, 16, 2, 8192, 1);
        String passA = "example";
        String passB = "another";
        String passC = "example";
        byte[] saltA = hasher.salt();
        byte[] saltB = hasher.salt();
        byte[] saltC = hasher.salt();
        String hashA = hasher.hash(passA, saltA);
        String hashB = hasher.hash(passB, saltB);
        String hashC = hasher.hash(passC, saltC);
        assertNotEquals(hashA, hashB);
        assertNotEquals(hashA, hashC);
        assertNotEquals(hashB, hashC);
    }
}