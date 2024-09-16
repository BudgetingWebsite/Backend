package org.amoseman.budgetingwebsitebackend.pojo.account;

import org.amoseman.budgetingwebsitebackend.pojo.Entity;
import org.amoseman.budgetingwebsitebackend.pojo.account.op.UpdateAccount;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class Account extends Entity {
    public final String hash;
    public final String salt;
    public final String roles;

    @ConstructorProperties({"uuid", "created", "updated", "hash", "salt", "roles"})
    public Account(String uuid, LocalDateTime created, LocalDateTime updated, String hash, String salt, String roles) {
        super(uuid, created, updated);
        this.hash = hash;
        this.salt = salt;
        this.roles = roles;
    }

    public Account(Account account, UpdateAccount update, LocalDateTime updated) {
        super(account.uuid, account.created, updated);
        this.hash = update.getHash();
        this.salt = update.getSalt();
        this.roles = update.getRoles();
    }
}
