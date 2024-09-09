package org.amoseman.budgetingwebsitebackend.exception;

public class InvalidFinanceEventTypeException extends Exception {
    public InvalidFinanceEventTypeException(String type) {
        super(String.format("The type %s is not valid", type));
    }
}
