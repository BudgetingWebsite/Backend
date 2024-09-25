package org.amoseman.budgetingbackend.password;

public enum ResultType {
    SUCCESS,
    INVALID_CHARACTER,
    LESS_THAN_MIN_LENGTH,
    MISSING_UPPERCASE,
    MISSING_SPECIAL,
    WEAK_SCORE
}
