package org.amoseman.budgetingwebsitebackend.pojo.account.op;

public class CreateAccount {
    private String username;
    private String password;

    public CreateAccount() {
    }

    public CreateAccount(String username, String password) {
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
