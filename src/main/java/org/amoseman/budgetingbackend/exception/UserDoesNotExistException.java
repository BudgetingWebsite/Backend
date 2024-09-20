package org.amoseman.budgetingbackend.exception;

public class UserDoesNotExistException extends EntityDoesNotExistException {
    public UserDoesNotExistException(String action, String id) {
        super(action, id, "account");
    }
}
