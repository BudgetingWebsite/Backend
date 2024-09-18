package org.amoseman.budgetingbackend.exception.mapping;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.DateTimeException;

@Provider
public class DateTimeExceptionMapper implements ExceptionMapper<DateTimeException> {
    @Override
    public Response toResponse(DateTimeException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }
}
