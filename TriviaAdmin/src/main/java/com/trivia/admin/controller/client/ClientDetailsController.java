package com.trivia.admin.controller.client;

import com.trivia.core.service.ClientService;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.Client;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@ViewScoped
public class ClientDetailsController implements Serializable {
    private @Inject ClientService clientService;
    private transient @Inject FacesContext facesContext;
    private Client client;

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        this.client = clientService.findById(id, EntityView.ClientDetails);
    }

    public Client getClient() {
        return client;
    }
}