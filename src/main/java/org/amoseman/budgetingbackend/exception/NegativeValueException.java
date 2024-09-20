package org.amoseman.budgetingbackend.exception;

/**
 * An exception for when negative values occurring when they shouldn't.
 */
public class NegativeValueException extends Exception {

    /**
     * Instantiate an exception for the occurrence of a negative value when it shouldn't.
     * @param value the negative value.
     */
    public NegativeValueException(long value) {
        super(String.format("Negative value of %d occurred", value));
    }
}
