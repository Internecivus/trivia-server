package com.trivia.admin.controller.question;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.exception.InvalidInputException;
import com.trivia.core.service.CategoryService;
import com.trivia.core.service.QuestionService;
import com.trivia.core.utility.ImageUtil;
import com.trivia.persistence.entity.Category;
import com.trivia.persistence.entity.Question;
import org.apache.commons.io.IOUtils;
import org.omnifaces.util.Ajax;
import org.primefaces.event.FileUploadEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Set;


@Named
@ViewScoped
public class QuestionCreateController implements Serializable {
    private @Inject QuestionService questionService;
    private @Inject CategoryService categoryService;
    private transient @Inject FacesContext facesContext;
    private Question question;
    private Set<Category> categoriesAvailable;
    private byte[] uploadedImage;

    @PostConstruct
    public void init() {
        this.question = new Question();
        this.categoriesAvailable = categoryService.getAll();
    }

    public void uploadImage(FileUploadEvent fileUploadEvent) {
        try {
            InputStream previewImageStream = ImageUtil.getPreviewImage(fileUploadEvent.getFile().getInputstream());
            uploadedImage = IOUtils.toByteArray(previewImageStream);
        }
        catch (IOException e) {
            Messages.addWarnFor("growl", i18n.get("failure"), i18n.get("upload.failure"));
        }
        catch (InvalidInputException e) {
            Messages.addErrorFor("growl", i18n.get("failure"), e.getMessage());
        }
        finally {
            Ajax.update("question_create:imageUpload"); // We need this to refresh the "rendered" attribute and fileUpload component.
        }
    }

    public void removeImage() {
        uploadedImage = null;
        Ajax.update("question_create:imageUpload");
    }

    public String create() {
        if (uploadedImage != null) {
            questionService.create(question, new ByteArrayInputStream(uploadedImage));
        }
        else {
            questionService.create(question);
        }
        Messages.addInfoGlobalFlash(i18n.get("success"), i18n.get("create.success"));
        return facesContext.getViewRoot().getViewId() + "?faces-redirect=true";
    }

    public void setQuestion(Question newQuestion) {
        this.question = newQuestion;
    }

    public Question getQuestion() {
        return question;
    }

    public Set<Category> getCategoriesAvailable() {
        return categoriesAvailable;
    }

    public byte[] getUploadedImage() {
        return uploadedImage;
    }
}