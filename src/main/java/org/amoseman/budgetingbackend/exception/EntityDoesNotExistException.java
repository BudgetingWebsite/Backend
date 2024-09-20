package org.amoseman.budgetingbackend.exception;

public class EntityDoesNotExistException extends Exception {
    public EntityDoesNotExistException(String action, String id, String object) {
        super(String.format("Attempted to %s %s %s, but it does not exist", action, object, id));
    }
}
