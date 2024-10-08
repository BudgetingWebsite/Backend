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
    private int maxUsernameLength;

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
}
