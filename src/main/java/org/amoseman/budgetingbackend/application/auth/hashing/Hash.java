package org.amoseman.budgetingbackend.application.auth.hashing;

import java.security.SecureRandom;

/**
 * Represents a hash algorithm.
 */
public abstract class Hash {
    protected final SecureRandom random;
    private final int saltLength;

    /**
     * Instantiate a new hash algorithm.
     * @param random the secure random to use.
     * @param saltLength the length of salt to use.
     */
    public Hash(SecureRandom random, int saltLength) {
        this.random = random;
        this.saltLength = saltLength;
    }

    /**
     * Generate a hash from the provided text and salt.
     * @param text the text to hash.
     * @param salt the salt to use in hashing.
     * @return the hash.
     */
    public abstract String hash(String text, byte[] salt);

    /**
     * Generate some salt.
     * @return the salt.
     */
    public byte[] salt() {
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);
        return salt;
    }
}
