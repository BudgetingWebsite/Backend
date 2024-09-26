package org.amoseman.budgetingbackend.exception.mapping;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.amoseman.budgetingbackend.exception.InvalidPasswordException;

@Provider
public class InvalidPasswordExceptionMapper implements ExceptionMapper<InvalidPasswordException> {
    @Override
    public Response toResponse(InvalidPasswordException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(e.getMessage())
                .build();
    }
}
