package org.amoseman.budgetingbackend.exception;

public class IdentifierDoesNotExistException extends Exception {
    public IdentifierDoesNotExistException(String action, String id, String object) {
        super(String.format("Attempted to %s %s %s, but it does not exist", action, object, id));
    }
}
