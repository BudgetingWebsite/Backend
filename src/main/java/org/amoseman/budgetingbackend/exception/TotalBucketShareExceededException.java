package org.amoseman.budgetingbackend.exception;

public class TotalBucketShareExceededException extends Exception {
    public TotalBucketShareExceededException(double total, double amount) {
        super(String.format("Attempted to add bucket that would put the current share sum %.4f over the 1.0 max with its %.4f share", total, amount));
    }
}
