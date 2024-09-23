package org.amoseman.budgetingbackend.application.auth;

import org.amoseman.budgetingbackend.application.auth.hashing.ArgonHash;
import org.amoseman.budgetingbackend.application.auth.hashing.Hash;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

class ArgonHashTest {

    @Test
    void hash() {
        SecureRandom random = new SecureRandom();
        Hash hash = new ArgonHash(random, 16, 16, 2, 8192, 1);
        String passA = "example";
        String passB = "another";
        String passC = "example";
        byte[] saltA = hash.salt();
        byte[] saltB = hash.salt();
        byte[] saltC = hash.salt();
        String hashA = hash.hash(passA, saltA);
        String hashB = hash.hash(passB, saltB);
        String hashC = hash.hash(passC, saltC);
        assertNotEquals(hashA, hashB);
        assertNotEquals(hashA, hashC);
        assertNotEquals(hashB, hashC);
    }
}