package org.amoseman.budgetingbackend.exception;

public class FinanceRecordAlreadyExistsException extends EntityAlreadyExistsException {
    public FinanceRecordAlreadyExistsException(String action, String id) {
        super(action, id, "finance record");
    }
}
