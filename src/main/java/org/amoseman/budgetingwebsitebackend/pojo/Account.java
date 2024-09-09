package org.amoseman.budgetingwebsitebackend.pojo;

import java.time.LocalDateTime;

/**
 * Represents a user account.
 */
public class Account extends Updatable {
    private final String passwordHash;

    /**
     * Instantiate a user account.
     * @param username the username of the account.
     * @param created the time when the account was created.
     * @param updated the time when the account was last updated.
     * @param passwordHash the hash of the account's password.
     */
    public Account(String username, LocalDateTime created, LocalDateTime updated, String passwordHash) {
        super(username, created, updated);
        this.passwordHash = passwordHash;
    }

    /**
     * Get the hash of the account's password.
     * @return the password hash.
     */
    public String getPasswordHash() {
        return passwordHash;
    }
}
