package org.amoseman.budgetingwebsitebackend.exception;

public class TotalPartitionShareExceededException extends Exception {
    public TotalPartitionShareExceededException(double total, double amount) {
        super(String.format("Attempted to add partition that would put the current share sum %.4f over the 1.0 max with its %.4f share", total, amount));
    }
}
