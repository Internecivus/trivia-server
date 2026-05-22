package com.trivia.admin.controller.category;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.exception.InvalidInputException;
import com.trivia.core.service.CategoryService;
import com.trivia.core.utility.ImageUtil;
import com.trivia.persistence.entity.Category;
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


@Named
@ViewScoped
public class CategoryCreateController implements Serializable {
    private @Inject CategoryService categoryService;
    private transient @Inject FacesContext facesContext;
    private Category category;
    private byte[] uploadedImage;

    @PostConstruct
    public void init() {
        this.category = new Category();
    }

    public String create() {
        if (uploadedImage == null) {
            facesContext.validationFailed();
            Messages.addErrorFor("category_create:image_upload", i18n.get("field.required"), i18n.get("field.required"));
            return null;
        }

        categoryService.create(category, new ByteArrayInputStream(uploadedImage));
        Messages.addInfoGlobalFlash(i18n.get("success"), i18n.get("create.success"));
        return facesContext.getViewRoot().getViewId() + "?faces-redirect=true";
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
            Ajax.update("category_create:imageUpload");
        }
    }

    public void removeImage() {
        uploadedImage = null;
        Ajax.update("category_create:imageUpload");
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public byte[] getUploadedImage() {
        return uploadedImage;
    }
}