package org.amoseman.budgetingwebsitebackend.pojo.account.op;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an account creation operation.
 */
public class CreateAccount {
    private String username;
    private String password;

    /**
     * Instantiate an account creation operation.
     * @param username the username of the new account.
     * @param password the password of the new account.
     */
    @JsonCreator
    public CreateAccount(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
