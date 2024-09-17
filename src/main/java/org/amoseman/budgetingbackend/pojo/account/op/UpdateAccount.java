package org.amoseman.budgetingbackend.pojo.account.op;

/**
 * Represents an account update operation.
 */
public class UpdateAccount {
    private String uuid;
    private String hash;
    private String salt;
    private String roles;

    public UpdateAccount() {

    }

    /**
     * Instantiate an account update.
     * The roles should be comma-delimited with no whitespaces and are case-sensitive.
     * Valid roles are ADMIN and USER.
     * @param uuid the current username of the account.
     * @param hash the updated password hash.
     * @param salt the updated password hash salt.
     * @param roles the updated roles.
     */
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
