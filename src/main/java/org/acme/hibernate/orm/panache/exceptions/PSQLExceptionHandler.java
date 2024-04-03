package org.acme.hibernate.orm.panache.exceptions;

import io.quarkus.arc.ArcUndeclaredThrowableException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

@Provider
public class PSQLExceptionHandler implements ExceptionMapper<ArcUndeclaredThrowableException> {

    @Override
    public Response toResponse(ArcUndeclaredThrowableException throwables) {
        return Response.status(500).entity("Deu merda").build();
    }

}
