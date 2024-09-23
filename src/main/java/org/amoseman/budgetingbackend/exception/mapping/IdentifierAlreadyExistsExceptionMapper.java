package org.amoseman.budgetingbackend.exception.mapping;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.amoseman.budgetingbackend.exception.EntityAlreadyExistsException;

@Provider
public class IdentifierAlreadyExistsExceptionMapper implements ExceptionMapper<EntityAlreadyExistsException> {
    @Override
    public Response toResponse(EntityAlreadyExistsException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }
}
