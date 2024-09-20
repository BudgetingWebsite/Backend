package org.amoseman.budgetingbackend.exception;

public class AccountDoesNotExistException extends EntityDoesNotExistException {
    public AccountDoesNotExistException(String action, String id) {
        super(action, id, "account");
    }
}
