package org.amoseman.budgetingbackend.password;

import org.amoseman.budgetingbackend.exception.InvalidPasswordException;

/**
 * The result of a password strength check.
 */
public class PasswordValidationResult {
    public final PasswordValidationType type;
    public final double entropyScore;
    public final double featureScore;

    /**
     * Instantiate a password strength check result.
     * @param type the type of result.
     * @param entropyScore the entropy score of the password.
     * @param featureScore the feature score of the password.
     */
    public PasswordValidationResult(PasswordValidationType type, double entropyScore, double featureScore) {
        this.type = type;
        this.entropyScore = entropyScore;
        this.featureScore = featureScore;
    }

    @Override
    public String toString() {
        return String.format("%s: %.4f, %.4f, %.4f", type, entropyScore, featureScore, entropyScore + featureScore);
    }

    public InvalidPasswordException asException() {
        return switch (type) {
            case SUCCESS -> null;
            case WEAK_SCORE -> new InvalidPasswordException("Password has weak score");
            case LESS_THAN_MIN_LENGTH -> new InvalidPasswordException("Password is not long enough");
            case MISSING_UPPERCASE -> new InvalidPasswordException("Password is missing an uppercase letter");
            case MISSING_SPECIAL -> new InvalidPasswordException("Password is missing a special character");
            case INVALID_CHARACTER -> new InvalidPasswordException("Password contains an invalid character");
        };
    }
}
