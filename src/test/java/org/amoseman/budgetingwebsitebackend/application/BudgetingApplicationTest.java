package org.amoseman.budgetingwebsitebackend.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.InitTestConfiguration;
import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingwebsitebackend.pojo.record.op.CreateIncome;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BudgetingApplicationTest {

    private void startApplication(String configurationLocation) {
        try {
            new BudgetingApplication().run("server", configurationLocation);
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

    private void init(String adminUsername, String adminPassword) {
        String databaseURL = "jdbc:sqlite:test-database.db";
        String configurationLocation = "test-config.yaml";
        InitTestDatabase.init(databaseURL, "schema.sql");
        InitTestConfiguration.init(configurationLocation, databaseURL, adminUsername, adminPassword);
        startApplication(configurationLocation);
    }

    @Test
    void test() {
        String address = "http://127.0.0.1:8080";
        String adminUsername = "admin_user";
        String adminPassword = "admin_pass";
        init(adminUsername, adminPassword);

        WebTarget client = JerseyClientBuilder.newClient()
                .target(address)
                .register(HttpAuthenticationFeature.basic(adminUsername, adminPassword));

        CreateIncome createIncome = new CreateIncome(100, 2024, 1, 1, "example", "example");
        String createIncomeJson = toJSON(createIncome);
        Entity<String> createIncomeEntity = Entity.entity(createIncomeJson, MediaType.APPLICATION_JSON_TYPE);
        Response response =  client.path("/record/income").request().post(createIncomeEntity);
        assertEquals(200, response.getStatus());
        response.close();

        /*
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

         */
    }
}