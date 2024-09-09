package org.amoseman.budgetingwebsitebackend.application;

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
}