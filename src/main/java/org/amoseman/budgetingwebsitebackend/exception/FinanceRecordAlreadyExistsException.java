package org.amoseman.budgetingwebsitebackend.exception;

public class FinanceRecordAlreadyExistsException extends IdentifierAlreadyExistsException {
    public FinanceRecordAlreadyExistsException(String action, String id) {
        super(action, id, "finance record");
    }
}
