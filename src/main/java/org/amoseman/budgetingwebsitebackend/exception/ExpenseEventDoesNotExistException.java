package org.amoseman.budgetingwebsitebackend.exception;

public class ExpenseEventDoesNotExistException extends IdentifierDoesNotExistException {
    public ExpenseEventDoesNotExistException(String action, String id) {
        super(action, id, "expense event");
    }
}
