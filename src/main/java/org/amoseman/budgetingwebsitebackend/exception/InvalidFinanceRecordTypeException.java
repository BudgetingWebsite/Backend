package org.amoseman.budgetingwebsitebackend.exception;

public class InvalidFinanceRecordTypeException extends Exception {
    public InvalidFinanceRecordTypeException(String type) {
        super(String.format("The type %s is not valid", type));
    }
}