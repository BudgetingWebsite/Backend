package org.amoseman.budgetingbackend.exception;

public class BucketDoesNotExistException extends EntityDoesNotExistException {
    public BucketDoesNotExistException(String action, String id) {
        super(action, id, "bucket");
    }
}
