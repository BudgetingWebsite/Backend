package org.amoseman.budgetingwebsitebackend.exception;

public class UserDoesNotExistException extends IdentifierDoesNotExistException {
    public UserDoesNotExistException(String action, String id) {
        super(action, id, "user");
    }
}
