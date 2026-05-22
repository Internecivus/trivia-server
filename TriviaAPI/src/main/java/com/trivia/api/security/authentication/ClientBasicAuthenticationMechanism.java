package com.trivia.api.security.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.util.Base64;



@AutoApplySession
@ApplicationScoped
public class ClientBasicAuthenticationMechanism implements HttpAuthenticationMechanism {
    private @Inject IdentityStore identityStore;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) {
        Credential credential = getCredential(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (!context.isProtected()) {
            return context.doNothing();
        }
        else {
            if (credential != null) {
                CredentialValidationResult credentialValidationResult = identityStore.validate(credential);
                if (credentialValidationResult.getStatus() == CredentialValidationResult.Status.VALID) {
                    return context.notifyContainerAboutLogin(credentialValidationResult);
                }
            }

            response.setHeader(HttpHeaders.WWW_AUTHENTICATE,"Basic realm=\"Trivia API\"");
            return context.responseUnauthorized();
        }
    }

    public Credential getCredential(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            try {
                String[] headerCredential = new String(Base64.getDecoder().decode(authorizationHeader.substring(6))).split(":");
                return new UsernamePasswordCredential(headerCredential[0], headerCredential[1]);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
        }
        return null;
    }
}