package org.amoseman.budgetingbackend.exception;

/**
 * An exception for when an entity is found to already exist.
 */
public class EntityAlreadyExistsException extends Exception {
    /**
     * Instantiate an exception for when an entity is found to already exist.
     * @param action the action resulted in the exception.
     * @param id the identifier of the already existing entity.
     * @param type the type of entity.
     */
    public EntityAlreadyExistsException(String action, String id, String type) {
        super(String.format("Attempted to %s %s, but ID %s already exists", action, type, id));
    }
}
