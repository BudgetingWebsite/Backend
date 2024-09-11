package org.amoseman.budgetingwebsitebackend;

import org.amoseman.fetch.CodeCondition;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestHandler implements ResponseHandler<String> {
    private final CodeCondition condition;

    public TestHandler(CodeCondition condition) {
        this.condition = condition;
    }

    @Override
    public String handleResponse(HttpResponse response) throws IOException {
        StatusLine line = response.getStatusLine();
        boolean success = condition.check(line.getStatusCode());
        String content = EntityUtils.toString(response.getEntity());
        if (!success) {
            System.out.println(content);
            String reason = String.format("%d %s %s", line.getStatusCode(), line.getReasonPhrase(), line.getProtocolVersion());
            fail(reason);
        }
        return content;
    }
}
