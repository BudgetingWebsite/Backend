package org.amoseman.budgetingbackend.exception;

public class BucketAlreadyExistsException extends EntityAlreadyExistsException {
    public BucketAlreadyExistsException(String action, String id) {
        super(action, id, "bucket");
    }
}
