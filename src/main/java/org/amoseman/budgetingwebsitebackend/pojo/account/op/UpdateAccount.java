package org.amoseman.budgetingwebsitebackend.pojo.account.op;

import java.util.Set;

public class UpdateAccount {
    private String uuid;
    private String hash;
    private String salt;
    private Set<String> roles;

    public UpdateAccount() {
    }

    public UpdateAccount(String uuid, String hash, String salt, Set<String> roles) {
        this.uuid = uuid;
        this.hash = hash;
        this.salt = salt;
        this.roles = roles;
    }

    public String getUuid() {
        return uuid;
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
