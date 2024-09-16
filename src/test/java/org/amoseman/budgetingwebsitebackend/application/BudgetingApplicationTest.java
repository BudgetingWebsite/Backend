package org.amoseman.budgetingwebsitebackend.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.amoseman.budgetingwebsitebackend.TestHandler;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateExpense;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateIncome;
import org.amoseman.budgetingwebsitebackend.pojo.partition.op.CreatePartition;
import org.amoseman.budgetingwebsitebackend.pojo.partition.op.UpdatePartition;
import org.amoseman.fetch.Fetch;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private JsonNode toNode(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(json);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String toJSON(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String get(String json, String field) {
        return toNode(json).get(field).asText();
    }

    private String get(String json, int index, String field) {
        return toNode(json).get(index).get(field).asText();
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
        String incomeJSON = toJSON(new CreateIncome(100, 2024, 1, 1, "example", "example"));
        String uuid = fetch.request("/event/income", "POST", incomeJSON, testSuccess);
        String events = fetch.request("/event/income", "GET", testSuccess);
        assertEquals(1, toNode(events).size());
        fetch.request("/event/income/" + uuid, "DELETE", testSuccess);
        events = fetch.request("/event/income", "GET", testSuccess);
        assertEquals(0, toNode(events).size());

        // partition create and event amount effect
        String uuidPartitionA = fetch.request("/partition", "POST", toJSON(new CreatePartition("essential", 0.5)), testSuccess);
        String uuidPartitionB = fetch.request("/partition", "POST", toJSON(new CreatePartition("savings", 0.2)), testSuccess);
        uuid = fetch.request(
                "/event/income",
                "POST",
                incomeJSON,
                testSuccess
        );
        String partitions = fetch.request("/partition", "GET", testSuccess);
        assertEquals("50", get(partitions, 0, "amount"));
        assertEquals("20", get(partitions, 1, "amount"));
        // partition update
        fetch.request("/partition/" + uuidPartitionA, "PUT", toJSON(new UpdatePartition("essential", 0.2)), testSuccess);
        fetch.request("/partition/" + uuidPartitionB, "PUT", toJSON(new UpdatePartition("savings", 0.7)), testSuccess);
        partitions = fetch.request("/partition", "GET", testSuccess);
        assertEquals("20", get(partitions, 0, "amount"));
        assertEquals("70", get(partitions, 1, "amount"));
        // add partition after event
        String uuidPartitionC = fetch.request("/partition", "POST", toJSON(new CreatePartition("other", 0.1)), testSuccess);
        partitions = fetch.request("/partition", "GET", testSuccess);
        assertEquals("20", get(partitions, 0, "amount"));
        assertEquals("70", get(partitions, 1, "amount"));
        assertEquals("10", get(partitions, 2, "amount"));
        // statistics resource
        String totalFunds = fetch.request("/stats/total", "GET", testSuccess);
        assertEquals("100", totalFunds);
        // add partition to exceed share limit
        String uuidPartitionD = fetch.request("/partition", "POST", toJSON(new CreatePartition("exceeds", 0.1)), testFailure);
        // add expense event
        String expenseJSON = toJSON(new CreateExpense(50, 2024, 1, 1, "example", "example", uuidPartitionB));
        String expense = fetch.request("/event/expense", "POST", expenseJSON, testSuccess);
        events = fetch.request("/event/expense", "GET", testSuccess);
        assertEquals(1, toNode(events).size());
        totalFunds = fetch.request("/stats/total", "GET", testSuccess);
        assertEquals("50", totalFunds);
        // remove event to change partition amounts
        fetch.request("/event/income/" + uuid, "DELETE", testSuccess);
        partitions = fetch.request("/partition", "GET", testSuccess);
        System.out.println(partitions);
        assertEquals("0", get(partitions, 0, "amount"));
        assertEquals("-50", get(partitions, 1, "amount"));
        assertEquals("0", get(partitions, 2, "amount"));

        //fail("Integration test not fully implemented");
    }
}