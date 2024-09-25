package org.amoseman.budgetingbackend.password;

import java.util.Optional;

/**
 * A class for calculating the bit entropy of a password.
 */
public class Entropy {
    /**
     * Calculate the bit entropy of a password.
     * @param password the password.
     * @return the bit entropy, or empty if it contained an invalid character.
     */
    public Optional<Double> entropy(String password) {
        int l = password.length();
        Optional<Integer> pool = new PoolCalculator().pool(password);
        if (pool.isEmpty()) {
            return Optional.empty();
        }
        int r = pool.get();
        double e = log2(Math.pow(r, l));
        return Optional.of(e);
    }

    /**
     * Calculate the log base 2 of the provided number.
     * @param x the number.
     * @return the log base 2 of the provided number.
     */
    private double log2(double x) {
        return Math.log(x) / Math.log(2);
    }
}
