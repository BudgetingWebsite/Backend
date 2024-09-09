package org.amoseman.budgetingwebsitebackend.exception;

public class PartitionAlreadyExistsException extends IdentifierAlreadyExistsException {
    public PartitionAlreadyExistsException(String action, String id) {
        super(action, id, "partition");
    }
}
