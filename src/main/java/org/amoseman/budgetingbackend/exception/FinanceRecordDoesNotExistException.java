package org.amoseman.budgetingbackend.exception;

public class FinanceRecordDoesNotExistException extends IdentifierDoesNotExistException {
    public FinanceRecordDoesNotExistException(String action, String id) {
        super(action, id, "finance record");
    }
}
