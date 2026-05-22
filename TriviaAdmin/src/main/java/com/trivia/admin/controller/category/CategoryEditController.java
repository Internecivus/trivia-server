package com.trivia.admin.controller.category;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.exception.InvalidInputException;
import com.trivia.core.service.CategoryService;
import com.trivia.core.utility.ImageUtil;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.Category;
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


@Named
@ViewScoped
public class CategoryEditController implements Serializable {
    private @Inject CategoryService categoryService;
    private transient @Inject FacesContext facesContext;
    private Category category;
    private byte[] uploadedImage;
    private boolean imageUploaded;

    @PostConstruct
    public void init() throws IOException {
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        this.category = categoryService.findById(id, EntityView.QuestionDetails);

        if (category.getImage() != null) {
            uploadedImage = ImageUtil.getImage(category.getImage());
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
            Ajax.update("category_edit:imageUpload");
        }
    }

    public void removeImage() {
        uploadedImage = null;
        imageUploaded = false;
        Ajax.update("category_edit:imageUpload");
    }

    public void delete() throws IOException {
        categoryService.deleteById(category.getId());
        Messages.addWarnFlashFor("growl", i18n.get("success"), i18n.get("delete.success"));
        facesContext.getExternalContext().redirect("/admin/categories/list.xhtml");
    }

    public void update() {
        // The image has been removed but a new one has not been added.
        if (!imageUploaded && uploadedImage == null) {
            facesContext.validationFailed();
            Messages.addErrorFor("category_edit:image_upload", i18n.get("field.required"), i18n.get("field.required"));
            return;
        }
        // Use a new image.
        else if (imageUploaded && uploadedImage != null) {
            categoryService.update(category, new ByteArrayInputStream(uploadedImage));
        }
        // Use the old image.
        else {
            categoryService.update(category);
        }


        Messages.addInfoGlobal(i18n.get("success"), i18n.get("update.success"));
        PrimeFaces.current().scrollTo("messages");
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
