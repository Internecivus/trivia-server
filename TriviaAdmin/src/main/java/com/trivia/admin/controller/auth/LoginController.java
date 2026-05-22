package com.trivia.admin.controller.auth;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.persistence.entity.User;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;



@Named
@ViewScoped
public class LoginController implements Serializable {
    @Inject private FacesContext facesContext;
    @Inject private SecurityContext securityContext;
    private User user;
    private boolean rememberMe;

    @PostConstruct
    // For some way this throws a warning about init() using IOException, but a return of String (as in using the String
    // redirect) is not even going to deploy. Is init() not supposed to redirect at all?
    public void init() throws IOException {
        user = new User();

        if (securityContext.getCallerPrincipal() != null) {
            facesContext.getExternalContext().redirect("/admin/index.xhtml");
        }
    }

    public void login() throws IOException {
        AuthenticationStatus authenticationStatus = securityContext.authenticate(
                (HttpServletRequest) facesContext.getExternalContext().getRequest(),
                (HttpServletResponse) facesContext.getExternalContext().getResponse(),
                AuthenticationParameters.withParams()
                        .credential(new UsernamePasswordCredential(user.getName(), user.getPassword()))
                        .newAuthentication(true)
                        .rememberMe(rememberMe)
        );

        if (authenticationStatus.equals(AuthenticationStatus.SEND_FAILURE)) {
            Messages.addErrorGlobal(i18n.get("failure"),i18n.get("login.failure"));
            facesContext.validationFailed();
        }
        else if (authenticationStatus.equals(AuthenticationStatus.SEND_CONTINUE)) {
            facesContext.responseComplete();
        }
        else if (authenticationStatus.equals(AuthenticationStatus.SUCCESS)) {
            facesContext.getExternalContext().redirect("/admin/index.xhtml");
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
