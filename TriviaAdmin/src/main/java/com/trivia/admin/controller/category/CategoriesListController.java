package com.trivia.admin.controller.category;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.service.CategoryService;
import com.trivia.persistence.entity.Category;
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
public class CategoriesListController implements Serializable {
    private @Inject CategoryService categoryService;
    private LazyDataModel<Category> lazyCategories;
    private String searchString;

    @PostConstruct
    public void init() {
        lazyCategories = new LazyDataModel<Category>() {
            @Override
            public List<Category> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                String searchString = (filters.get("globalFilter") != null) ? filters.get("globalFilter").toString() : null;

                List<Category> result = categoryService.findAll(
                    first / pageSize + 1,
                    pageSize,
                    sortField,
                    com.trivia.core.utility.SortOrder.valueOf(sortOrder.toString()),
                    searchString
                );
                super.setRowCount(categoryService.getLastCount());
                return result;
            }
        };
    }

    public void delete(int id) {
        categoryService.deleteById(id);
        Messages.addWarnFor("growl", i18n.get("success"), i18n.get("delete.success"));
    }

    public LazyDataModel<Category> getLazyCategories() {
        return lazyCategories;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}