package org.amoseman.budgetingbackend.password;

import org.amoseman.budgetingbackend.application.BudgetingConfiguration;

import java.util.Optional;

/**
 * A class for checking the strength of passwords.
 */
public class PasswordChecker {
    private final int minLength;
    private final int minEntropy;
    private final double minScore;
    private final boolean requiresUppercase;
    private final boolean requiresSpecial;

    /**
     * Instantiate a password checker.
     * @param configuration the configuration to use.
     */
    public PasswordChecker(BudgetingConfiguration configuration) {
        minLength = configuration.getMinPasswordLength();
        minEntropy = configuration.getMinPasswordEntropy();
        minScore = configuration.getMinPasswordScore();
        requiresUppercase = configuration.isPasswordRequiresUppercase();
        requiresSpecial = configuration.isPasswordRequiresSpecial();
    }

    /**
     * Check the strength of a password.
     * @param password the password.
     * @return the result of the check.
     */
    public PasswordValidationResult check(String password) {
        Optional<Double> entropy = new Entropy().entropy(password);
        if (entropy.isEmpty()) {
            return new PasswordValidationResult(PasswordValidationType.INVALID_CHARACTER, 0, 0);
        }
        double entropyScore = entropy.get() / minEntropy;
        double featureScore = new BadFeature().score(password);

        if (password.length() < minLength) {
            return new PasswordValidationResult(PasswordValidationType.LESS_THAN_MIN_LENGTH, entropyScore, featureScore);
        }
        if (requiresUppercase && !containsUppercase(password)) {
            return new PasswordValidationResult(PasswordValidationType.MISSING_UPPERCASE, entropyScore, featureScore);
        }
        if (requiresSpecial && !containsSpecialCharacter(password)) {
            return new PasswordValidationResult(PasswordValidationType.MISSING_SPECIAL, entropyScore, featureScore);
        }
        if (entropyScore + featureScore < minScore * 2) {
            return new PasswordValidationResult(PasswordValidationType.WEAK_SCORE, entropyScore, featureScore);
        }
        return new PasswordValidationResult(PasswordValidationType.SUCCESS, entropyScore, featureScore);
    }

    /**
     * Check if the provided password contains an uppercase letter.
     * @param password the password.
     * @return true if the password contains an uppercase letter, false otherwise.
     */
    private boolean containsUppercase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the provided password contains a special letter.
     * @param password the password.
     * @return true if the password contains a special letter, false otherwise.
     */
    private boolean containsSpecialCharacter(String password) {
        for (char c : password.toCharArray()) {
            if (PoolCalculator.SPECIAL.contains("" + c)) {
                return true;
            }
        }
        return false;
    }
}
