package org.amoseman.budgetingbackend.password;

import java.util.Optional;

public class PoolCalculator {
    public static final String SPECIAL = "!@#$%^&*()-_=+{}[]<>?/,.|`~;:";

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
