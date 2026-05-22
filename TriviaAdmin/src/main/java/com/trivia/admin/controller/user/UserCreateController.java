package com.trivia.admin.controller.user;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.service.RoleService;
import com.trivia.core.service.UserService;
import com.trivia.persistence.entity.Role;
import com.trivia.persistence.entity.User;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;


@Named
@ViewScoped
public class UserCreateController implements Serializable {
    private @Inject UserService userService;
    private @Inject RoleService roleService;
    private transient @Inject FacesContext facesContext;
    private User user;
    private Set<Role> rolesAvailable;
    private String providerSecret;

    @PostConstruct
    public void init() {
        this.user = new User();
        this.rolesAvailable = roleService.getAll();
    }

    public String create() {
        User createdUser = userService.create(user);
        providerSecret = createdUser.getProviderSecret();
        if (providerSecret != null) {
            PrimeFaces.current().executeScript("PF('secretDialog').show();");
            return null;
        }
        else {
            Messages.addInfoGlobalFlash(i18n.get("success"), i18n.get("create.success"));
            return facesContext.getViewRoot().getViewId() + "?faces-redirect=true";
        }
    }

    public void closeSecretDialog(CloseEvent closeEvent) throws IOException {
        Messages.addInfoGlobalFlash(i18n.get("success"), i18n.get("create.success"));
        facesContext.getExternalContext().redirect(facesContext.getViewRoot().getViewId() + "?faces-redirect=true");
    }

    public String getProviderSecret() {
        return providerSecret;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Role> getRolesAvailable() {
        return rolesAvailable;
    }
}