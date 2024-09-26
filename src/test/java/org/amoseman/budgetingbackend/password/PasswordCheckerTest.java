package org.amoseman.budgetingbackend.password;

import org.amoseman.budgetingbackend.application.BudgetingConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordCheckerTest {

    @Test
    void check() {
        PasswordChecker checker = new PasswordChecker(new BudgetingConfiguration());
        assertEquals(PasswordValidationType.INVALID_CHARACTER, checker.check(" ").type);
        assertEquals(PasswordValidationType.LESS_THAN_MIN_LENGTH, checker.check("abcdefg").type);
        assertEquals(PasswordValidationType.MISSING_UPPERCASE, checker.check("abcdefgh").type);
        assertEquals(PasswordValidationType.MISSING_SPECIAL, checker.check("Abcdefgh").type);
        assertEquals(PasswordValidationType.WEAK_SCORE, checker.check("Abcdefg!").type);
        assertEquals(PasswordValidationType.SUCCESS, checker.check("AT*bjfwb9238t").type);
    }
}