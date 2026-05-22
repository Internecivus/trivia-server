package com.trivia.admin.controller;

import com.trivia.admin.security.authentication.UserCallerPrincipal;
import com.trivia.persistence.entity.User;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;


@Named
@Dependent
public class ViewController {
    private @Inject SecurityContext securityContext;
    private @Inject FacesContext facesContext;

    private String path;
    private String fullName; // Uses dot ('.') as a delimiter.
    private String name;
    private User currentUser;

    private String[] folders = {"admin", "public"};

    public ViewController() {}

    @PostConstruct
    public void init() {
        path = getPathFromUri(Faces.getViewId());
        fullName = path
            .substring(1)
            .replaceFirst("WEB-INF/", "")
            .replaceFirst("(" + String.join("|", folders) + ")/", "")
            .replaceAll("/", ".");
        name = fullName.substring(fullName.lastIndexOf('.') + 1);

        if (securityContext.getCallerPrincipal() != null) {
            currentUser = ((UserCallerPrincipal) securityContext.getCallerPrincipal()).getUser();
        }
    }

    public String getPathFromUri(String uri) {
        return uri.substring(0, uri.lastIndexOf('.'));
    }

    public String getPathFromOutcome(String outcome) {
        String pathWithoutName = path.substring(0, path.lastIndexOf(name) - 1);
        return pathWithoutName + "/" + outcome;
    }

    public boolean pathIs(String path) {
        return (path.equals(this.path));
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}