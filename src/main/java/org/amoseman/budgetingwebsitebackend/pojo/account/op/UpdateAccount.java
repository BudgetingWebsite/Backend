package org.amoseman.budgetingwebsitebackend.pojo.account.op;

public class UpdateAccount {
    private String uuid;
    private String hash;
    private String salt;
    private String roles;

    public UpdateAccount() {
    }

    public UpdateAccount(String uuid, String hash, String salt, String roles) {
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

    public String getRoles() {
        return roles;
    }
}
