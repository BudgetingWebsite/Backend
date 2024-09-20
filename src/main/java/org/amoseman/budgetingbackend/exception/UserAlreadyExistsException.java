package org.amoseman.budgetingbackend.exception;

public class UserAlreadyExistsException extends IdentifierAlreadyExistsException {
    public UserAlreadyExistsException(String action, String id) {
        super(action, id, "account");
    }
}
