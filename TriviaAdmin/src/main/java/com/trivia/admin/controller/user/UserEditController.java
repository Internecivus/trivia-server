package com.trivia.admin.controller.user;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.service.RoleService;
import com.trivia.core.service.UserService;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.Role;
import com.trivia.persistence.entity.User;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;


@Named
@ViewScoped
public class UserEditController implements Serializable {
    private @Inject UserService userService;
    private @Inject RoleService roleService;
    private @Inject SecurityContext securityContext;
    private transient @Inject FacesContext facesContext;
    private User user;
    private Set<Role> rolesAvailable;
    private String newPassword;
    private String providerSecret;

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        this.user = userService.findById(id, EntityView.UserDetails);
        this.rolesAvailable = roleService.getAll();
    }

    public void delete() throws IOException {
        userService.deleteById(user.getId());
        Messages.addWarnFlashFor("growl", i18n.get("success"), i18n.get("delete.success"));
        facesContext.getExternalContext().redirect("/admin/users/list.xhtml");
    }

    public void update() {
        if (newPassword != null) user.setPassword(newPassword);
        User updatedUser = userService.update(user);

        // Was not updated.
        if (updatedUser.getProviderSecret() == null) {
            Messages.addWarnGlobal(i18n.get("success"), i18n.get("auth.update")); // TODO: Needs a more hard reset. This is a security issue too.
            PrimeFaces.current().scrollTo("messages");
        }
        else {
            providerSecret = updatedUser.getProviderSecret();
            PrimeFaces.current().executeScript("PF('secretDialog').show();");
        }
    }

    public void closeSecretDialog(CloseEvent closeEvent) {
        Messages.addWarnGlobal(i18n.get("success"), i18n.get("auth.update"));
        PrimeFaces.current().scrollTo("messages");
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

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
