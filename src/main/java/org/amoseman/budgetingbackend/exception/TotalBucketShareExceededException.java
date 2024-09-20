package org.amoseman.budgetingbackend.exception;

/**
 * An exception for when a user's total bucket share exceeds 1.
 */
public class TotalBucketShareExceededException extends Exception {
    /**
     * Instantiate exception as a user's total bucket share has exceeded 1.
     * @param total the current total share.
     * @param amount the share that would have made the total greater than 1.
     */
    public TotalBucketShareExceededException(double total, double amount) {
        super(String.format("Attempted to add bucket that would put the current share sum %.4f over the 1.0 max with its %.4f share", total, amount));
    }
}
