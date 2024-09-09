package org.amoseman.budgetingwebsitebackend.exception;

public class IdentifierDoesNotExistException extends Exception {
    public IdentifierDoesNotExistException(String action, String id, String object) {
        super(String.format("Attempted to %s, but ID %s does not exist for %s", action, id, object));
    }
}
