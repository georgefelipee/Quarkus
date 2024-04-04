package org.acme.hibernate.orm.panache.exceptions;

import io.quarkus.arc.ArcUndeclaredThrowableException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PSQLExceptionHandler implements ExceptionMapper<ArcUndeclaredThrowableException> {

    @Override
    public Response toResponse(ArcUndeclaredThrowableException throwables) {
        return Response.status(500).entity(new ErrorResponseEdit("Erro interno do servidor")).build();
    }

}
