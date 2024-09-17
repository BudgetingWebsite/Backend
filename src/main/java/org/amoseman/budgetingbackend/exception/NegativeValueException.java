package org.amoseman.budgetingbackend.exception;

public class NegativeValueException extends Exception {
    public NegativeValueException(long value) {
        super(String.format("Negative value of %d occurred", value));
    }
}
