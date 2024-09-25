package org.amoseman.budgetingbackend.password;

/**
 * Represents the type of result from a password strength check.
 * SUCCESS              - The check is a success.
 * INVALID_CHARACTER    - The password contains an invalid character, such as a whitespace.
 * LESS_THAN_MIN_LENGTH - The password is shorter than the configured minimum length.
 * MISSING_UPPERCASE    - The password is missing an uppercase letter.
 * MISSING_SPECIAL      - The password is missing a special character.
 * WEAK_SCORE           - The password has a poor score based on its entropy and bad features, which are repetition and sequences.
 */
public enum ResultType {
    SUCCESS,
    INVALID_CHARACTER,
    LESS_THAN_MIN_LENGTH,
    MISSING_UPPERCASE,
    MISSING_SPECIAL,
    WEAK_SCORE
}
