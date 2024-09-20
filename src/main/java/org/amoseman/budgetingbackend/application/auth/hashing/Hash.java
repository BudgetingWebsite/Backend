package org.amoseman.budgetingbackend.application.auth.hashing;

import java.security.SecureRandom;

public abstract class Hash {
    protected final SecureRandom random;
    private final int saltLength;

    public Hash(SecureRandom random, int saltLength) {
        this.random = random;
        this.saltLength = saltLength;
    }

    public abstract String hash(String text, byte[] salt);

    public byte[] salt() {
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);
        return salt;
    }
}
