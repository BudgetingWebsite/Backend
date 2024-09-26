package org.amoseman.budgetingbackend.exception;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String reason) {
        super(reason);
    }
}
