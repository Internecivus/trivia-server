package com.trivia.admin.controller.client;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.service.ClientService;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.Client;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;


@Named
@ViewScoped
public class ClientEditController implements Serializable {
    private @Inject ClientService clientService;
    private transient @Inject FacesContext facesContext;
    private Client client;
    private String clientSecret;

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        this.client = clientService.findById(id, EntityView.ClientDetails);
    }

    public void generateNewAPISecret() {
        clientSecret = clientService.generateNewAPISecret(client);
        PrimeFaces.current().scrollTo("messages");
        PrimeFaces.current().executeScript("PF('secretDialog').show();");
    }

    public void delete() throws IOException {
        clientService.deleteById(client.getId());
        Messages.addWarnFlashFor("growl", i18n.get("success"), i18n.get("delete.success"));
        facesContext.getExternalContext().redirect("/admin/clients/list.xhtml");
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
