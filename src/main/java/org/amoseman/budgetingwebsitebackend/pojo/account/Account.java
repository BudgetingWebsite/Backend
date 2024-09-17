package org.amoseman.budgetingwebsitebackend.pojo.account;

import org.amoseman.budgetingwebsitebackend.pojo.Entity;
import org.amoseman.budgetingwebsitebackend.pojo.account.op.UpdateAccount;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

/**
 * Represents an account.
 */
public class Account extends Entity {
    public final String hash;
    public final String salt;
    public final String roles;

    /**
     * Instantiate an account.
     * The roles should be comma-delimited with no whitespaces and are case-sensitive.
     * Valid roles are ADMIN and USER.
     * @param uuid the username of the account.
     * @param created when the account was created.
     * @param updated when the account was last updated.
     * @param hash the password hash of the account.
     * @param salt the password hash salt of the account.
     * @param roles the roles of the account.
     */
    @ConstructorProperties({"uuid", "created", "updated", "hash", "salt", "roles"})
    public Account(String uuid, LocalDateTime created, LocalDateTime updated, String hash, String salt, String roles) {
        super(uuid, created, updated);
        this.hash = hash;
        this.salt = salt;
        this.roles = roles;
    }

    /**
     * Update an account.
     * @param account the account to update.
     * @param update the update information.
     * @param updated when this updated occurred.
     */
    public Account(Account account, UpdateAccount update, LocalDateTime updated) {
        super(account.uuid, account.created, updated);
        this.hash = update.getHash();
        this.salt = update.getSalt();
        this.roles = update.getRoles();
    }
}
