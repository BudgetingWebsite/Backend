package org.amoseman.budgetingbackend.password;

import java.util.Optional;

public class PasswordChecker {
    private static final int MIN_LENGTH = 8;
    private static final int MIN_ENTROPY = 75;
    private static final int MAX_BAD_FEATURE_SCORE = 4;
    private static final double MIN_SCORE = 0.9;

    public Result check(String password) {
        if (password.length() < MIN_LENGTH) {
            return new Result(ResultType.LESS_THAN_MIN_LENGTH, 0);
        }
        Optional<Double> entropy = new Entropy().entropy(password);
        if (entropy.isEmpty()) {
            return new Result(ResultType.INVALID_CHARACTER, 0);
        }
        double badFeatureScore = new BadFeature().score(password);
        double score = score(entropy.get(), badFeatureScore);
        if (score < MIN_SCORE) {
            return new Result(ResultType.WEAK_SCORE, score);
        }
        return new Result(ResultType.SUCCESS, score);
    }

    private double score(double entropy, double badFeatureScore) {
        double e = entropy / MIN_ENTROPY;
        double f = (MAX_BAD_FEATURE_SCORE - badFeatureScore) / MAX_BAD_FEATURE_SCORE;
        System.out.printf("%.4f, %.4f\n", e, f);
        return (e + f) / 2;
    }
}
