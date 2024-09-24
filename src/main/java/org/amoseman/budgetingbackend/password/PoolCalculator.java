package org.amoseman.budgetingbackend.password;

import java.util.Optional;

public class PoolCalculator {
    private static final String OTHER = "!@#$%^&*()-_=+{}[]<>?/";

    public Optional<Integer> pool(String password) {
        int lowercase = 0;
        int uppercase = 0;
        int numeric = 0;
        int other = 0;
        char[] characters = password.toCharArray();
        for (char c : characters) {
            if (Character.isAlphabetic(c)) {
                if (Character.isLowerCase(c)) {
                    lowercase = 1;
                }
                else {
                    uppercase = 1;
                }
            }
            else if (Character.isDigit(c)) {
                numeric = 1;
            }
            else if (OTHER.contains("" + c)) {
                other = 1;
            }
            else {
                return Optional.empty();
            }
        }
        return Optional.of(poolSize(lowercase, uppercase, numeric, other));
    }

    private int poolSize(int lowercase, int uppercase, int numeric, int other) {
        return 26 * lowercase + 26 * uppercase + 10 * numeric + OTHER.length() * other;
    }
}
