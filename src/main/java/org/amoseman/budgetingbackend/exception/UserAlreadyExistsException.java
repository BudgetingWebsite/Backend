package org.amoseman.budgetingbackend.exception;

public class UserAlreadyExistsException extends IdentifierDoesNotExistException {
    public UserAlreadyExistsException(String action, String id) {
        super(action, id, "account");
    }
}
