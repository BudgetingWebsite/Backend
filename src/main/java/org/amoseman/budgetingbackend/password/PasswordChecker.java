package org.amoseman.budgetingbackend.password;

import java.util.Optional;

public class PasswordChecker {
    public static void main(String[] args) {
        String password = "A1b2c3!@#$";
        Result result = new PasswordChecker().check(password);
        System.out.println(result);
    }
    private static final int MIN_LENGTH = 8;
    private static final int MIN_ENTROPY = 85;
    private static final int MAX_BAD_FEATURE_SCORE = 4;
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
        if (entropyScore < MIN_SCORE || featureScore < MIN_SCORE) {
            return new Result(ResultType.WEAK_SCORE, entropyScore, featureScore);
        }
        return new Result(ResultType.SUCCESS, entropyScore, featureScore);
    }
}
