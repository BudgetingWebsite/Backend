package org.amoseman.budgetingbackend.exception;

public class FinanceRecordDoesNotExistException extends EntityDoesNotExistException {
    public FinanceRecordDoesNotExistException(String action, String id) {
        super(action, id, "finance record");
    }
}
