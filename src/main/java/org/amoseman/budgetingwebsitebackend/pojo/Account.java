package org.amoseman.budgetingwebsitebackend.pojo;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents a user account.
 */
public class Account extends Updatable {
    private final String passwordHash;
    private final Set<String> roles;

    /**
     * Instantiate a user account.
     * @param username the username of the account.
     * @param created the time when the account was created.
     * @param updated the time when the account was last updated.
     * @param passwordHash the hash of the account's password.
     * @param roles the roles of the account.
     */
    public Account(String username, LocalDateTime created, LocalDateTime updated, String passwordHash, Set<String> roles) {
        super(username, created, updated);
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    /**
     * Get the hash of the account's password.
     * @return the password hash.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Get the roles of the account.
     * @return the roles.
     */
    public Set<String> getRoles() {
        return roles;
    }
}
