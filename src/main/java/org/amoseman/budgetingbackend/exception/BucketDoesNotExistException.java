package org.amoseman.budgetingbackend.exception;

public class BucketDoesNotExistException extends IdentifierDoesNotExistException {
    public BucketDoesNotExistException(String action, String id) {
        super(action, id, "bucket");
    }
}
