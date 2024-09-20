package org.amoseman.budgetingbackend.exception.mapping;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.amoseman.budgetingbackend.exception.UsernameExceedsMaxLengthException;

@Provider
public class UsernameExceedsMaxLengthExceptionMapper implements ExceptionMapper<UsernameExceedsMaxLengthException> {

    @Override
    public Response toResponse(UsernameExceedsMaxLengthException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }
}
