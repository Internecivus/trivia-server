package com.trivia.api.api.v1;

import com.trivia.api.security.authentication.ClientBasicAuthenticationMechanism;
import com.trivia.core.service.ClientService;
import com.trivia.persistence.entity.Client;

import javax.inject.Inject;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Base64;



@Path("/client")
public class ClientEndpoint {
    private @Inject ClientService clientService;

    @POST
    public Response postClient(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @DefaultValue("false") @QueryParam("register") boolean register
    ) {
        if (register) {
            UsernamePasswordCredential credential = (UsernamePasswordCredential) new ClientBasicAuthenticationMechanism()
                .getCredential(authorizationHeader);

            Client clientEntity = clientService.registerFor(credential.getCaller(), credential.getPasswordAsString());

            String credentials = Base64.getEncoder().encodeToString(
                (clientEntity.getApiKey() + ":" + clientEntity.getApiSecret()).getBytes());

            Response response = Response
                .status(Response.Status.CREATED)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + credentials)
                .build();

            return response;
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
