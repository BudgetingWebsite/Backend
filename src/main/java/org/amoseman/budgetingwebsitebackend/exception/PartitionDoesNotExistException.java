package org.amoseman.budgetingwebsitebackend.exception;

public class PartitionDoesNotExistException extends IdentifierDoesNotExistException {
    public PartitionDoesNotExistException(String action, String id) {
        super(action, id, "partition");
    }
}
