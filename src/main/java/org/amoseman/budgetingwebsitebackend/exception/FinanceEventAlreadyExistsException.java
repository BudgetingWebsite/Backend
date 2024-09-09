package org.amoseman.budgetingwebsitebackend.exception;

public class FinanceEventAlreadyExistsException extends IdentifierAlreadyExistsException {
    public FinanceEventAlreadyExistsException(String action, String id) {
        super(action, id, "finance event");
    }
}
