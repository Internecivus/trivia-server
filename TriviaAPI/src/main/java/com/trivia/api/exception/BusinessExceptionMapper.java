package com.trivia.api.exception;

import com.trivia.core.exception.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

// This can be broken into multiple mappers as the number of exceptions grows.
// TODO: e instanceof EJBAccessException
@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException e) {
        if (e instanceof EntityNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        else if (e instanceof EntityExistsException) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        else if (e instanceof InvalidCredentialException ||
                 e instanceof NotAuthorizedException) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (e instanceof InvalidInputException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        else if (e instanceof SystemException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
