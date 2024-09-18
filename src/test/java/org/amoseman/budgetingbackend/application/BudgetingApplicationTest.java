package org.amoseman.budgetingbackend.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.amoseman.InitTestConfiguration;
import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingbackend.pojo.record.info.IncomeInfo;
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

        IncomeInfo createIncome = new IncomeInfo(100, 2024, 1, 1, "example", "example");
        String createIncomeJson = toJSON(createIncome);
        Entity<String> createIncomeEntity = Entity.entity(createIncomeJson, MediaType.APPLICATION_JSON_TYPE);
        Response response =  client.path("/record/income").request().post(createIncomeEntity);
        assertEquals(200, response.getStatus());
        response.close();

        InitTestDatabase.close("jdbc:sqlite:test.db");
    }
}