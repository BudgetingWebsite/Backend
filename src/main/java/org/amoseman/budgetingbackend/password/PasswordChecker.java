package org.amoseman.budgetingbackend.password;

import org.amoseman.budgetingbackend.application.BudgetingConfiguration;

import java.util.Optional;

public class PasswordChecker {
    private final int minLength;
    private final int minEntropy;
    private final int MIN_BAD_FEATURE_SCORE = 3;
    private final double minScore;
    private final boolean requiresUppercase;
    private final boolean requiresSpecial;

    public PasswordChecker(BudgetingConfiguration configuration) {
        minLength = configuration.getMinPasswordLength();
        minEntropy = configuration.getMinPasswordEntropy();
        minScore = configuration.getMinPasswordScore();
        requiresUppercase = configuration.isPasswordRequiresUppercase();
        requiresSpecial = configuration.isPasswordRequiresSpecial();
    }

    public Result check(String password) {
        Optional<Double> entropy = new Entropy().entropy(password);
        if (entropy.isEmpty()) {
            return new Result(ResultType.INVALID_CHARACTER, 0, 0);
        }
        double entropyScore = entropy.get() / minEntropy;
        double badFeatures = new BadFeature().score(password);
        double featureScore = (MIN_BAD_FEATURE_SCORE - badFeatures) / MIN_BAD_FEATURE_SCORE;

        if (password.length() < minLength) {
            return new Result(ResultType.LESS_THAN_MIN_LENGTH, entropyScore, featureScore);
        }
        if (requiresUppercase && !containsUppercase(password)) {
            return new Result(ResultType.MISSING_UPPERCASE, entropyScore, featureScore);
        }
        if (requiresSpecial && !containsSpecialCharacter(password)) {
            return new Result(ResultType.MISSING_SPECIAL, entropyScore, featureScore);
        }
        if (entropyScore + featureScore < minScore * 2) {
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
