package org.amoseman.budgetingbackend.exception;

public class UsernameExceedsMaxLengthException extends Exception {
    public UsernameExceedsMaxLengthException(int maxLength, String username) {
        super(String.format("Username %s exceeds max length of %d", username, maxLength));
    }
}
