package org.amoseman.budgetingbackend.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.WebTarget;
import org.amoseman.InitTestConfiguration;
import org.amoseman.InitTestDatabase;
import org.amoseman.StatusTest;
import org.amoseman.StatusTester;
import org.amoseman.budgetingbackend.model.account.op.CreateAccount;
import org.amoseman.budgetingbackend.model.bucket.BucketInfo;
import org.amoseman.budgetingbackend.model.record.info.ExpenseInfo;
import org.amoseman.budgetingbackend.model.record.info.IncomeInfo;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BudgetingApplicationTest {

    static void startApplication(String configurationLocation) {
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

    static final String address = "http://127.0.0.1:8080";
    static final String adminUsername = "admin_user";
    static final String adminPassword = "admin_pass";
    static final String databaseURL = "jdbc:h2:mem:test";
    static final String configurationLocation = "test-config.yaml";

    static final StatusTest successTest = (code) -> code > 199 && code < 300;
    static StatusTest failureTest = (code) -> code > 299;

    static WebTarget client;
    static StatusTester tester;

    @BeforeAll
    static void setup() {
        InitTestDatabase.init(databaseURL, "schema.sql");
        InitTestConfiguration.init(configurationLocation, databaseURL, adminUsername, adminPassword);
        startApplication(configurationLocation);
        client = JerseyClientBuilder.newClient()
                .target(address)
                .register(HttpAuthenticationFeature.basic(adminUsername, adminPassword));
        tester = new StatusTester(client);
    }

    @AfterAll
    static void cleanup() {
        InitTestDatabase.clean(databaseURL);
    }

    @Test
    void accountCRUD() {
        CreateAccount create = new CreateAccount("alice", "password");
        tester.post("/account", toJSON(create), successTest);

        WebTarget alice = JerseyClientBuilder.newClient()
                    .target(address)
                    .register(HttpAuthenticationFeature.basic("alice", "password"));
        StatusTester aliceTester = new StatusTester(alice);
        aliceTester.put("/account/password", "{password: different}", successTest);

        tester.put("/account/alice/roles", "USER,ADMIN", successTest);
        tester.delete("/account/alice", successTest);
    }

    @Test
    void bucketCRUD() {
        BucketInfo create = new BucketInfo("bucket", 0.5);
        String uuid = tester.post("/bucket", toJSON(create), successTest);
        BucketInfo update = new BucketInfo("different", 0.3);
        tester.put("/bucket/" + uuid, toJSON(update), successTest);
        tester.get("/bucket", successTest);
        tester.delete("/bucket/" + uuid, successTest);
    }

    @Test
    void incomeCRUD() {
        IncomeInfo create = new IncomeInfo(0, 1, 1, 1, "", "");
        String uuid = tester.post("/record/income", toJSON(create), successTest);
        IncomeInfo update = new IncomeInfo(1, 1, 1, 1, "", "");
        tester.put("/record/income/" + uuid, toJSON(update), successTest);
        tester.get("/record/income/", new String[]{"startYear=1", "startMonth=1", "startDay=1", "endYear=2", "endMonth=1", "endDay=1"}, successTest);
        tester.delete("/record/income/" + uuid, successTest);
    }

    @Test
    void expenseCRUD() {
        String bucket = tester.post("/bucket", toJSON(new BucketInfo("example", 0.5)), successTest); // some SQL dialects require foreign key references to exist, such as H2, but not SQLite
        ExpenseInfo create = new ExpenseInfo(0, 1, 1, 1, "", "", bucket);
        String uuid = tester.post("/record/expense", toJSON(create), successTest);
        ExpenseInfo update = new ExpenseInfo(1, 1, 1, 1, "", "", bucket);
        tester.put("/record/expense/" + uuid, toJSON(update), successTest);
        tester.get("/record/expense/", new String[]{"startYear=1", "startMonth=1", "startDay=1", "endYear=2", "endMonth=1", "endDay=1"}, successTest);
        tester.delete("/record/expense/" + uuid, successTest);
    }
}