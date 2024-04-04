package org.acme.hibernate.orm.panache.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundException implements ExceptionMapper<jakarta.ws.rs.NotFoundException> {

    @Override
    public Response toResponse(jakarta.ws.rs.NotFoundException e) {
        return Response.status(404).entity(new ErrorResponseEdit(e.getMessage())).build();
    }
}
