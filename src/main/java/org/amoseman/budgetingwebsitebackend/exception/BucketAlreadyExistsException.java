package org.amoseman.budgetingwebsitebackend.exception;

public class BucketAlreadyExistsException extends IdentifierAlreadyExistsException {
    public BucketAlreadyExistsException(String action, String id) {
        super(action, id, "bucket");
    }
}
