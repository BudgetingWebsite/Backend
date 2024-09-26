package org.amoseman.budgetingbackend.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;

public class BudgetingConfiguration extends Configuration {
    @NotEmpty
    private String databaseURL;
    @NotEmpty
    private String adminUsername;
    @NotEmpty
    private String adminPassword;
    private int maxUsernameLength = 32;
    private int minPasswordLength = 8;
    private int minPasswordEntropy = 85;
    private double minPasswordScore = 0.75;
    private boolean passwordRequiresUppercase = true;
    private boolean passwordRequiresSpecial = true;

    @JsonProperty("database-url")
    public String getDatabaseURL() {
        return databaseURL;
    }

    @JsonProperty("admin-username")
    public String getAdminUsername() {
        return adminUsername;
    }

    @JsonProperty("admin-password")
    public String getAdminPassword() {
        return adminPassword;
    }

    @JsonProperty("max-username-length")
    public int getMaxUsernameLength() {
        return maxUsernameLength;
    }

    @JsonProperty("min-password-length")
    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    @JsonProperty("min-password-entropy")
    public int getMinPasswordEntropy() {
        return minPasswordEntropy;
    }

    @JsonProperty("min-password-score")
    public double getMinPasswordScore() {
        return minPasswordScore;
    }

    @JsonProperty("password-requires-uppercase")
    public boolean isPasswordRequiresUppercase() {
        return passwordRequiresUppercase;
    }

    @JsonProperty("password-requires-special")
    public boolean isPasswordRequiresSpecial() {
        return passwordRequiresSpecial;
    }

    public BudgetingConfiguration setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
        return this;
    }

    public BudgetingConfiguration setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
        return this;
    }

    public BudgetingConfiguration setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
        return this;
    }

    public BudgetingConfiguration setMaxUsernameLength(int maxUsernameLength) {
        this.maxUsernameLength = maxUsernameLength;
        return this;
    }

    public BudgetingConfiguration setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
        return this;
    }

    public BudgetingConfiguration setMinPasswordEntropy(int minPasswordEntropy) {
        this.minPasswordEntropy = minPasswordEntropy;
        return this;
    }

    public BudgetingConfiguration setMinPasswordScore(double minPasswordScore) {
        this.minPasswordScore = minPasswordScore;
        return this;
    }

    public BudgetingConfiguration setPasswordRequiresUppercase(boolean passwordRequiresUppercase) {
        this.passwordRequiresUppercase = passwordRequiresUppercase;
        return this;
    }

    public BudgetingConfiguration setPasswordRequiresSpecial(boolean passwordRequiresSpecial) {
        this.passwordRequiresSpecial = passwordRequiresSpecial;
        return this;
    }
}
