package org.acme.hibernate.orm.panache.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestException implements ExceptionMapper<jakarta.ws.rs.BadRequestException> {
    @Override
    public Response toResponse(jakarta.ws.rs.BadRequestException e) {
        return Response.status(400).entity(new ErrorResponseEdit(e.getMessage())).build();
    }
}
