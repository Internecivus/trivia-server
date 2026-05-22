package com.trivia.api.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(javax.validation.ConstraintViolationException e) {
        ConstraintViolation cv = (ConstraintViolation) e.getConstraintViolations();
        return Response.status(Response.Status.BAD_REQUEST).entity(cv).build();
    }
}
