package org.amoseman.budgetingwebsitebackend.pojo;

import org.amoseman.budgetingwebsitebackend.pojo.update.AccountUpdate;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents a user account.
 */
public class Account extends Updatable<AccountUpdate> {
    private String passwordHash;
    private String passwordSalt;
    private Set<String> roles;

    /**
     * Instantiate a user account.
     * @param username the username of the account.
     * @param created the time when the account was created.
     * @param updated the time when the account was last updated.
     * @param passwordHash the hash of the account's password.
     * @param passwordSalt the salt of the password hash.
     * @param roles the roles of the account.
     */
    public Account(String username, LocalDateTime created, LocalDateTime updated, String passwordHash, String passwordSalt, Set<String> roles) {
        super(username, created, updated);
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    @Override
    public void updateData(AccountUpdate update) {
        this.passwordHash = update.getPasswordHash();
        this.roles = update.getRoles();
    }

    /**
     * Get the hash of the account's password.
     * @return the password hash.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Get the salt of the account's password hash.
     * @return the password hash salt.
     */
    public String getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * Get the roles of the account.
     * @return the roles.
     */
    public Set<String> getRoles() {
        return roles;
    }
}
