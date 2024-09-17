package org.amoseman.budgetingbackend.application.auth;

import org.amoseman.budgetingbackend.pojo.account.Account;

import java.security.Principal;
import java.util.Set;

public class User implements Principal {
    private final String username;
    private final Set<String> roles;

    public User(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public User(Account account) {
        this.username = account.uuid;
        this.roles = Set.of(account.roles.split(","));
    }

    @Override
    public String getName() {
        return username;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
