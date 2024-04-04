package org.acme.hibernate.orm.panache.exceptions;


import io.quarkus.resteasy.reactive.server.runtime.exceptionmappers.UnauthorizedExceptionMapper;

import jakarta.annotation.Priority;

import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class UnauthorizedException extends UnauthorizedExceptionMapper implements ExceptionMapper<io.quarkus.security.UnauthorizedException> {


    @Override
    public Response toResponse(io.quarkus.security.UnauthorizedException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponseEdit("Erro de autenticação")).build();
    }
}
