package org.amoseman.budgetingbackend.password;

import java.util.Optional;

/**
 * A class for calculating the bit entropy of a password.
 */
public class PasswordEntropyCalculator {
    /**
     * Calculate the bit entropy of a password.
     * @param password the password.
     * @return the bit entropy, or empty if it contained an invalid character.
     */
    public Optional<Double> entropy(String password) {
        int l = password.length();
        Optional<Integer> pool = new CharacterPoolCalculator().pool(password);
        if (pool.isEmpty()) {
            return Optional.empty();
        }
        int r = pool.get();
        double e = Math.log((Math.pow(r, l))) / Math.log(2); // equivalent to log_2(r^l)
        return Optional.of(e);
    }
}
