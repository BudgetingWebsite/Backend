package org.amoseman.budgetingbackend.exception;

public class AccountAlreadyExistsException extends EntityAlreadyExistsException {
    public AccountAlreadyExistsException(String action, String id) {
        super(action, id, "account");
    }
}
