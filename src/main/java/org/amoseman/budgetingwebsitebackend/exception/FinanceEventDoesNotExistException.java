package org.amoseman.budgetingwebsitebackend.exception;

public class FinanceEventDoesNotExistException extends IdentifierDoesNotExistException {
    public FinanceEventDoesNotExistException(String action, String id) {
        super(action, id, "finance event");
    }
}
