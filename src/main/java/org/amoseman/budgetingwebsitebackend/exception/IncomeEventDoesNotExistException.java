package org.amoseman.budgetingwebsitebackend.exception;

public class IncomeEventDoesNotExistException extends IdentifierDoesNotExistException {
    public IncomeEventDoesNotExistException(String action, String id) {
        super(action, id, "income event");
    }
}
