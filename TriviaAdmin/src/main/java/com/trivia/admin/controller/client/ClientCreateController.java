package com.trivia.admin.controller.client;

import com.trivia.admin.controller.ViewController;
import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.exception.InvalidCredentialException;
import com.trivia.core.service.ClientService;
import com.trivia.core.service.RoleService;
import com.trivia.persistence.entity.Client;
import com.trivia.persistence.entity.RoleType;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;



@Named
@ViewScoped
public class ClientCreateController implements Serializable {
    private @Inject ClientService clientService;
    private @Inject RoleService roleService;
    private transient @Inject ViewController viewController;
    private transient @Inject FacesContext facesContext;
    private String providerKey;
    private String providerSecret;
    private String clientSecret;

    @PostConstruct
    public void init() {}

    public void registerForCurrent() {
        Client client = clientService.registerForCurrent();
        this.clientSecret = client.getApiSecret();
        PrimeFaces.current().executeScript("PF('secretDialog').show();");
        // FIXME: Does not show. Same goes for the ones below.
        Messages.addInfoGlobalFlash(i18n.get("success"), i18n.get("create.success"));
    }

    public void registerFor() {
        Client client;
        providerKey = providerKey.trim();
        try {
            if (viewController.getCurrentUser().hasRole(RoleType.ADMIN)) {
                client = clientService.registerFor(providerKey);
            }
            else {
                client = clientService.registerFor(providerKey, providerSecret);
            }
            this.clientSecret = client.getApiSecret();
            PrimeFaces.current().executeScript("PF('secretDialog').show();");
            Messages.addInfoGlobalFlash(i18n.get("success"), i18n.get("create.success"));

        }
        catch (InvalidCredentialException e) {
            Messages.addErrorGlobalFlash(i18n.get("failure"),i18n.get("login.failure"));
            facesContext.validationFailed();
        }
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public String getProviderSecret() {
        return providerSecret;
    }

    public void setProviderSecret(String providerSecret) {
        this.providerSecret = providerSecret;
    }
}