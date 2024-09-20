package org.amoseman.budgetingbackend.exception;

public class EntityAlreadyExistsException extends Exception {
    public EntityAlreadyExistsException(String action, String id, String object) {
        super(String.format("Attempted to %s %s, but ID %s already exists", action, id, object));
    }
}
