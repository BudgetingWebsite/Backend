package org.amoseman.budgetingwebsitebackend.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;

public class BudgetingConfiguration extends Configuration {
    @NotEmpty
    private String databaseURL;

    @JsonProperty("database-url")
    public String getDatabaseURL() {
        return databaseURL;
    }
}
