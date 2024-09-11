package org.amoseman.budgetingwebsitebackend.application;

import org.amoseman.budgetingwebsitebackend.TestHandler;
import org.amoseman.fetch.Fetch;
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

    private void startApplication(String configurationLocation, String databaseLocation) {
        try {
            new BudgetingApplication().run("server", configurationLocation);
            new File(databaseLocation.split(":")[2]).deleteOnExit();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void test() {
        String address = "http://127.0.0.1:8080";
        String configurationLocation = "test-config.yaml";
        String databaseLocation = "jdbc:sqlite:test-database.db";
        String adminUsername = "admin_user";
        String adminPassword = "admin_pass";
        generateConfigurationFile(configurationLocation, databaseLocation, adminUsername, adminPassword);
        startApplication(configurationLocation, databaseLocation);

        Fetch fetch = new Fetch.Builder()
                .setDomain(address)
                .setUsername(adminUsername)
                .setPassword(adminPassword)
                .build();

        TestHandler testSuccess = new TestHandler((code) -> code <= 299);
        TestHandler testFailure = new TestHandler((code) -> code > 299);
        // event create, retrieve, and delete
        String json = "{\"type\":\"income\",\"amount\":100,\"year\":2024,\"month\":1,\"day\":1,\"category\":\"example\",\"description\":\"example\"}";
        String uuid = fetch.request("/event/income", "POST", json, testSuccess);
        String events = fetch.request("/event/income", "GET", testSuccess);
        assertTrue(events.contains("\"owner\":\"admin_user\",\"type\":\"income\",\"amount\":100,\"occurred\":[2024,1,1,0,0],\"category\":\"example\",\"description\":\"example\""));
        fetch.request("/event/income/" + uuid, "DELETE", testSuccess);
        events = fetch.request("/event/income", "GET", testSuccess);
        assertFalse(events.contains("\"owner\":\"admin_user\",\"type\":\"income\",\"amount\":100,\"occurred\":[2024,1,1,0,0],\"category\":\"example\",\"description\":\"example\""));
        // partition create and event amount effect
        String uuidPartitionA = fetch.request("/partition", "POST", "{\"name\":\"essential\",\"share\":0.5}", testSuccess);
        String uuidPartitionB = fetch.request("/partition", "POST", "{\"name\":\"savings\",\"share\":0.2}", testSuccess);
        uuid = fetch.request("/event/income", "POST", json, testSuccess);
        String partitions = fetch.request("/partition", "GET", testSuccess);
        assertTrue(partitions.contains("\"name\":\"essential\",\"share\":0.5,\"amount\":50"));
        assertTrue(partitions.contains("\"name\":\"savings\",\"share\":0.2,\"amount\":20"));
        // partition update
        fetch.request("/partition/" + uuidPartitionA, "PUT", "{\"name\":\"essential\",\"share\":0.2}", testSuccess);
        fetch.request("/partition/" + uuidPartitionB, "PUT", "{\"name\":\"savings\",\"share\":0.7}", testSuccess);
        partitions = fetch.request("/partition", "GET", testSuccess);
        assertTrue(partitions.contains("\"name\":\"essential\",\"share\":0.2,\"amount\":20}"));
        assertTrue(partitions.contains("\"name\":\"savings\",\"share\":0.7,\"amount\":70"));
        // add partition after event
        String uuidPartitionC = fetch.request("/partition", "POST", "{\"name\":\"other\",\"share\":0.1}", testSuccess);
        partitions = fetch.request("/partition", "GET", testSuccess);
        assertTrue(partitions.contains("\"name\":\"essential\",\"share\":0.2,\"amount\":20}"));
        assertTrue(partitions.contains("\"name\":\"savings\",\"share\":0.7,\"amount\":70"));
        assertTrue(partitions.contains("\"name\":\"other\",\"share\":0.1,\"amount\":10"));
        // add partition to exceed share limit
        String uuidPartitionD = fetch.request("/partition", "POST", "{\"name\":\"other\",\"share\":0.1}", testFailure);

        fail("Integration test not fully implemented");
    }
}