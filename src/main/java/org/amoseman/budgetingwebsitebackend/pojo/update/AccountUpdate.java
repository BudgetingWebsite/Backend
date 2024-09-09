package org.amoseman.budgetingwebsitebackend.pojo.update;

import java.time.LocalDateTime;
import java.util.Set;

public class AccountUpdate extends Update {
    private final String passwordHash;
    private final Set<String> roles;

    public AccountUpdate(LocalDateTime updated, String passwordHash, Set<String> roles) {
        super(updated);
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
