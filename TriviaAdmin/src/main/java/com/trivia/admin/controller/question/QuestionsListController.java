package com.trivia.admin.controller.question;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.service.QuestionService;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.Question;
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
public class QuestionsListController implements Serializable {
    private @Inject QuestionService questionService;
    private LazyDataModel<Question> lazyQuestions;
    private String searchString;

    @PostConstruct
    public void init() {
        lazyQuestions = new LazyDataModel<Question>() {
            @Override
            public List<Question> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                String searchString = (filters.get("globalFilter") != null) ? filters.get("globalFilter").toString() : null;

                List<Question> result = questionService.findAll(
                    first / pageSize + 1,
                    pageSize,
                    sortField,
                    com.trivia.core.utility.SortOrder.valueOf(sortOrder.toString()),
                    searchString,
                    EntityView.QuestionList
                );
                super.setRowCount(questionService.getLastCount());
                return result;
            }
        };
    }

    public void delete(int id) {
        questionService.deleteById(id);
        Messages.addWarnFor("growl", i18n.get("success"), i18n.get("delete.success"));
    }

    public LazyDataModel<Question> getLazyQuestions() {
        return lazyQuestions;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}