package org.amoseman.budgetingwebsitebackend.application;

import org.amoseman.fetch.Fetch;
import org.apache.http.protocol.ResponseContent;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

class BudgetingApplicationTest {

    private void generateConfigurationFile(String configurationLocation, String databaseLocation, String username, String password) {
        File file = new File(configurationLocation);
        file.deleteOnExit();
        try {
            if (!file.createNewFile()) {
                return;
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String contents =   "database-url: " + databaseLocation + "\n" +
                                "admin-username: " + username + "\n" +
                                "admin-password: " + password + "\n" +
                                "max-username-length: 32";
            writer.write(contents);
            writer.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startApplication(String configurationLocation) {
        try {
            new BudgetingApplication().run("server", configurationLocation);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void test() {
        String address = "127.0.0.1:8080";
        String configurationLocation = "test-config.yaml";
        String databaseLocation = "jdbc:sqlite:test-database.db";
        String adminUsername = "admin_user";
        String adminPassword = "admin_pass";
        generateConfigurationFile(configurationLocation, databaseLocation, adminUsername, adminPassword);
        startApplication(configurationLocation);

        Fetch fetch = new Fetch.Builder()
                .setDomain(address)
                .setUsername(adminUsername)
                .setPassword(adminPassword)
                .build();
    }
}