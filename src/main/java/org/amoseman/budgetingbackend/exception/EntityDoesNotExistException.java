package org.amoseman.budgetingbackend.exception;

/**
 * An exception for when an entity is found to not exist.
 */
public class EntityDoesNotExistException extends Exception {
    /**
     * Instantiate an exception for when an entity is found to not exist.
     * @param action the action that resulted in the exception.
     * @param id the identifier of the non-existent entity.
     * @param type the type of the non-existent entity.
     */
    public EntityDoesNotExistException(String action, String id, String type) {
        super(String.format("Attempted to %s %s %s, but it does not exist", action, type, id));
    }
}
