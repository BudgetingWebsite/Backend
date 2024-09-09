package org.amoseman.budgetingwebsitebackend.application.auth;

import org.amoseman.budgetingwebsitebackend.pojo.Account;

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
        this.username = account.getIdentifier();
        this.roles = account.getRoles();
    }

    @Override
    public String getName() {
        return username;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
