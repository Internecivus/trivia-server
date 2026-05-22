package com.trivia.admin.controller.user;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.service.UserService;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.User;
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
public class UsersListController implements Serializable {
    private @Inject UserService userService;
    private LazyDataModel<User> lazyUsers;
    private String searchString;

    @PostConstruct
    public void init() {
        lazyUsers = new LazyDataModel<User>() {
            @Override
            public List<User> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                String searchString = (filters.get("globalFilter") != null) ? filters.get("globalFilter").toString() : null;

                List<User> result = userService.findAll(
                    first / pageSize + 1,
                    pageSize,
                    sortField,
                    com.trivia.core.utility.SortOrder.valueOf(sortOrder.toString()),
                    searchString,
                    EntityView.UserDetails
                );
                super.setRowCount(userService.getLastCount());
                return result;
            }
        };
    }

    public void delete(int id) {
        userService.deleteById(id);
        Messages.addWarnFor("growl", i18n.get("success"), i18n.get("delete.success"));
    }

    public LazyDataModel<User> getLazyUsers() {
        return lazyUsers;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}