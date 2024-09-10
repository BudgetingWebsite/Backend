package org.amoseman.budgetingwebsitebackend;

import org.amoseman.fetch.CodeCondition;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class TestHandler implements ResponseHandler<String> {
    private final CodeCondition condition;

    public TestHandler(CodeCondition condition) {
        this.condition = condition;
    }

    @Override
    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        StatusLine line = response.getStatusLine();
        boolean success = condition.check(line.getStatusCode());
        InputStream stream = response.getEntity().getContent();
        byte[] data = stream.readAllBytes();
        if (!success) {
            System.err.println(new String(data));
            String reason = String.format("%d %s %s", line.getStatusCode(), line.getReasonPhrase(), line.getProtocolVersion());
            fail(reason);
        }
        return new String(data);
    }
}
