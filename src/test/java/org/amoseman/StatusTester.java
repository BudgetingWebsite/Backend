package org.amoseman;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.fail;

public class StatusTester {
    private final WebTarget client;

    public StatusTester(WebTarget client) {
        this.client = client;
    }

    private Invocation.Builder request(String path) {
        return client.path(path).request();
    }

    private void test(StatusTest test, Response response) {
        if (!test.run(response.getStatus())) {
            fail(String.format("%d %s: %s", response.getStatus(), response.getStatusInfo().getReasonPhrase(), response.readEntity(String.class)));
        }
    }

    public String get(String path, StatusTest test) {
        Response response = request(path).get();
        test(test, response);
        String entity = response.readEntity(String.class);
        response.close();
        return entity;
    }

    public String get(String path, String[] params, StatusTest test) {
        WebTarget builder = client.path(path);
        for (String param : params) {
            String[] parts = param.split("=");
            builder = builder.queryParam(parts[0], parts[1]);
        }
        Response response = builder.request().get();
        test(test, response);
        String entity = response.readEntity(String.class);
        response.close();
        return entity;
    }

    public String post(String path, String contents, StatusTest test) {
        Response response = request(path).post(Entity.entity(contents, MediaType.APPLICATION_JSON_TYPE));
        test(test, response);
        String entity = response.readEntity(String.class);
        response.close();
        return entity;
    }

    public String put(String path, String contents, StatusTest test) {
        Response response = request(path).put(Entity.entity(contents, MediaType.APPLICATION_JSON_TYPE));
        test(test, response);
        String entity = response.readEntity(String.class);
        response.close();
        return entity;
    }

    public String delete(String path, StatusTest test) {
        Response response = request(path).delete();
        test(test, response);
        String entity = response.readEntity(String.class);
        response.close();
        return entity;
    }
}
