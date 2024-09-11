package org.amoseman.budgetingwebsitebackend.pojo.account;

import org.amoseman.budgetingwebsitebackend.pojo.Entity;
import org.amoseman.budgetingwebsitebackend.pojo.account.op.UpdateAccount;
import org.amoseman.budgetingwebsitebackend.time.Now;

import java.time.LocalDateTime;
import java.util.Set;

public class Account extends Entity {
    private final String hash;
    private final String salt;
    private final Set<String> roles;

    public Account(String uuid, LocalDateTime created, LocalDateTime updated, String hash, String salt, Set<String> roles) {
        super(uuid, created, updated);
        this.hash = hash;
        this.salt = salt;
        this.roles = roles;
    }

    public Account update(UpdateAccount update) {
        return new Account(
                uuid,
                created,
                Now.get(),
                update.getHash(),
                update.getSalt(),
                update.getRoles()
        );
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
