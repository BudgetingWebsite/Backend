package org.amoseman.budgetingwebsitebackend.pojo;

public class CreateAccount {
    private final String username;
    private final String password;

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
