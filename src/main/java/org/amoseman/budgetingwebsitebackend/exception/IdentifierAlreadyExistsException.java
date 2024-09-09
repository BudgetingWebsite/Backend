package org.amoseman.budgetingwebsitebackend.exception;

public class IdentifierAlreadyExistsException extends Exception {
    public IdentifierAlreadyExistsException(String action, String id, String object) {
        super(String.format("Attempted to %s, but ID %s already exists for %s", action, id, object));
    }
}
