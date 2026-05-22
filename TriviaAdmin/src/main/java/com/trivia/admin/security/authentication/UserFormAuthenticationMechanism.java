package com.trivia.admin.security.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@AutoApplySession
@LoginToContinue(
        loginPage = "/public/login.xhtml",
        errorPage = "",
        useForwardToLogin = false
)
@ApplicationScoped
public class UserFormAuthenticationMechanism implements HttpAuthenticationMechanism {
    private @Inject IdentityStore identityStore;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) {
        Credential credential = context.getAuthParameters().getCredential();

        if (credential != null) {
            return context.notifyContainerAboutLogin(identityStore.validate(credential));
        }
        else {
            return context.doNothing();
        }
    }
}