package com.trivia.api.security.authentication;

import com.trivia.core.exception.BusinessException;
import com.trivia.core.exception.CredentialException;
import com.trivia.core.service.ClientService;
import com.trivia.persistence.entity.Client;
import com.trivia.persistence.entity.RoleType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.HashSet;


@ApplicationScoped
public class ClientIdentityStore implements IdentityStore {
    @Inject private ClientService clientService;

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        Client client;

        String apiKey = credential.getCaller();
        String apiSecret = credential.getPasswordAsString();

        try {
            client = clientService.validateCredential(apiKey, apiSecret);
        }
        catch (CredentialException e) {
            return CredentialValidationResult.INVALID_RESULT;
        }
        catch (BusinessException e) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }

        return new CredentialValidationResult(new ClientCallerPrincipal(client), new HashSet<>(Arrays.asList(RoleType.Name.CLIENT, RoleType.Name.PRINCIPAL)));
    }
}