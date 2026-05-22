package com.trivia.admin.controller.client;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.service.ClientService;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.Client;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Named
@ViewScoped
public class ClientsListController implements Serializable {
    private @Inject ClientService clientService;
    private LazyDataModel<Client> lazyClients;
    private String searchString;

    @PostConstruct
    public void init() {
        lazyClients = new LazyDataModel<Client>() {
            @Override
            public List<Client> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                String searchString = (filters.get("globalFilter") != null) ? filters.get("globalFilter").toString() : null;

                List<Client> result = clientService.findAll(
                    first / pageSize + 1,
                    pageSize,
                    sortField,
                    com.trivia.core.utility.SortOrder.valueOf(sortOrder.toString()),
                    searchString,
                    EntityView.ClientDetails
                );
                super.setRowCount(clientService.getLastCount());
                return result;
            }
        };
    }

    public void delete(int id) {
        clientService.deleteById(id);
        Messages.addWarnFor("growl", i18n.get("success"), i18n.get("delete.success"));
    }

    public LazyDataModel<Client> getLazyClients() {
        return lazyClients;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}