package com.trivia.admin.controller.question;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.exception.InvalidInputException;
import com.trivia.core.service.CategoryService;
import com.trivia.core.service.QuestionService;
import com.trivia.core.utility.ImageUtil;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.Category;
import com.trivia.persistence.entity.Question;
import org.apache.commons.io.IOUtils;
import org.omnifaces.util.Ajax;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Set;


@Named
@ViewScoped
public class QuestionEditController implements Serializable {
    private @Inject QuestionService questionService;
    private @Inject CategoryService categoryService;
    private transient @Inject FacesContext facesContext;
    private Question question;
    private Set<Category> categoriesAvailable;
    private boolean imageUploaded;
    private byte[] uploadedImage;

    @PostConstruct
    public void init() throws IOException{
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        this.question = questionService.findById(id, EntityView.QuestionDetails);
        this.categoriesAvailable = categoryService.getAll();

        if (question.getImage() != null) {
            uploadedImage = ImageUtil.getImage(question.getImage());
        }
        imageUploaded = false;
    }

    public void uploadImage(FileUploadEvent fileUploadEvent) {
        try {
            InputStream previewImageStream = ImageUtil.getPreviewImage(fileUploadEvent.getFile().getInputstream());
            uploadedImage = IOUtils.toByteArray(previewImageStream);
            imageUploaded = true;
        }
        catch (IOException e) {
            Messages.addWarnFor("growl", i18n.get("failure"), i18n.get("upload.failure"));
        }
        catch (InvalidInputException e) {
            Messages.addErrorFor("growl", i18n.get("failure"), e.getMessage());
        }
        finally {
            Ajax.update("question_edit:imageUpload"); // We need this to refresh the "rendered" attribute and fileUpload component.
        }
    }

    public void removeImage() {
        question.setImage(null);
        uploadedImage = null;
        Ajax.update("question_edit:imageUpload");
    }

    public void delete() throws IOException {
        questionService.deleteById(question.getId());
        Messages.addWarnFlashFor("growl", i18n.get("success"), i18n.get("delete.success"));
        facesContext.getExternalContext().redirect("/admin/questions/list.xhtml");
    }

    public void update() {
        if (imageUploaded && uploadedImage != null) {
            questionService.update(question, new ByteArrayInputStream(uploadedImage));
        }
        else {
            questionService.update(question);
        }
        Messages.addInfoGlobal(i18n.get("success"), i18n.get("update.success"));
        PrimeFaces.current().scrollTo("messages");
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<Category> getCategoriesAvailable() {
        return categoriesAvailable;
    }

    public byte[] getUploadedImage() {
        return uploadedImage;
    }
}
