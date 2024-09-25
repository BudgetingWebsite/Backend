package org.amoseman.budgetingbackend.password;

import java.util.Optional;

/**
 * A class for calculate the pool size of a password.
 */
public class PoolCalculator {
    public static final String SPECIAL = "!@#$%^&*()-_=+{}[]<>?/,.|`~;:";

    /**
     * Calculate the pool size of a password.
     * @param password the password.
     * @return the pool size, or empty if it contains an invalid character.
     */
    public Optional<Integer> pool(String password) {
        int lowercase = 0;
        int uppercase = 0;
        int numeric = 0;
        int special = 0;
        char[] characters = password.toCharArray();
        for (char c : characters) {
            if (Character.isAlphabetic(c)) {
                if (Character.isLowerCase(c)) {
                    lowercase = 26;
                }
                else {
                    uppercase = 26;
                }
            }
            else if (Character.isDigit(c)) {
                numeric = 10;
            }
            else if (SPECIAL.contains("" + c)) {
                special = SPECIAL.length();
            }
            else {
                return Optional.empty();
            }
        }
        return Optional.of(lowercase + uppercase + numeric + special);
    }
}
