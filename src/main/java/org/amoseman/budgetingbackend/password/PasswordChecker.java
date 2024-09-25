package org.amoseman.budgetingbackend.password;

import java.util.Optional;

public class PasswordChecker {
    public static void main(String[] args) {
        String password = "a1b2c3!@#$aaaa";
        Result result = new PasswordChecker().check(password);
        System.out.println(result);
    }
    private static final int MIN_LENGTH = 8;
    private static final int MIN_ENTROPY = 85;
    private static final int MAX_BAD_FEATURE_SCORE = 3;
    private static final double MIN_SCORE = 0.75;

    public Result check(String password) {
        Optional<Double> entropy = new Entropy().entropy(password);
        if (entropy.isEmpty()) {
            return new Result(ResultType.INVALID_CHARACTER, 0, 0);
        }
        double entropyScore = entropy.get() / MIN_ENTROPY;
        double badFeatures = new BadFeature().score(password);
        double featureScore = (MAX_BAD_FEATURE_SCORE - badFeatures) / MAX_BAD_FEATURE_SCORE;

        if (password.length() < MIN_LENGTH) {
            return new Result(ResultType.LESS_THAN_MIN_LENGTH, entropyScore, featureScore);
        }
        if (!containsUppercase(password)) {
            return new Result(ResultType.MISSING_UPPERCASE, entropyScore, featureScore);
        }
        if (!containsSpecialCharacter(password)) {
            return new Result(ResultType.MISSING_SPECIAL, entropyScore, featureScore);
        }
        if (entropyScore + featureScore < MIN_SCORE * 2) {
            return new Result(ResultType.WEAK_SCORE, entropyScore, featureScore);
        }
        return new Result(ResultType.SUCCESS, entropyScore, featureScore);
    }

    private boolean containsUppercase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSpecialCharacter(String password) {
        for (char c : password.toCharArray()) {
            if (PoolCalculator.SPECIAL.contains("" + c)) {
                return true;
            }
        }
        return false;
    }
}
