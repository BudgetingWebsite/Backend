package org.amoseman.budgetingbackend.exception.mapping;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.amoseman.budgetingbackend.exception.TotalBucketShareExceededException;

@Provider
public class TotalBucketShareExceededExceptionMapper implements ExceptionMapper<TotalBucketShareExceededException> {
    @Override
    public Response toResponse(TotalBucketShareExceededException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }
}
