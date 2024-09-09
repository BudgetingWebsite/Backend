package org.amoseman.budgetingwebsitebackend.pojo.update;

import java.time.LocalDateTime;
import java.util.Set;

public class AccountUpdate extends Update {
    private final String passwordHash;
    private final String passwordSalt;
    private final Set<String> roles;

    public AccountUpdate(LocalDateTime updated, String passwordHash, String passwordSalt, Set<String> roles) {
        super(updated);
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.roles = roles;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
