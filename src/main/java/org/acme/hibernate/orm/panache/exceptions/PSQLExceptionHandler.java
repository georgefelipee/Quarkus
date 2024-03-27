package org.acme.hibernate.orm.panache.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.postgresql.util.PSQLException;

@Provider
public class PSQLExceptionHandler implements ExceptionMapper<PSQLException> {

    @Override
    public Response toResponse(PSQLException throwables) {
        return Response.status(500).entity("Deu merda").build();
    }

}
