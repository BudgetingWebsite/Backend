package org.amoseman.budgetingbackend.password;

public enum ResultType {
    SUCCESS,
    LESS_THAN_MIN_LENGTH,
    MISSING_SPECIAL,
    MISSING_UPPERCASE,
    INVALID_CHARACTER,
    WEAK_SCORE
}
