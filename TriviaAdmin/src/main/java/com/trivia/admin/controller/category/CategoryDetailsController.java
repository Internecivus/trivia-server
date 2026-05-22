package com.trivia.admin.controller.category;

import com.trivia.core.service.CategoryService;
import com.trivia.core.utility.ImageUtil;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.Category;

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
public class CategoryDetailsController implements Serializable {
    private @Inject CategoryService categoryService;
    private transient @Inject FacesContext facesContext;
    private Category category;
    private byte[] uploadedImage;

    @PostConstruct
    public void init() throws IOException {
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        this.category = categoryService.findById(id, EntityView.QuestionDetails);

        if (category.getImage() != null) {
            uploadedImage = ImageUtil.getImage(category.getImage());
        }
    }

    public Category getCategory() {
        return category;
    }

    public byte[] getUploadedImage() {
        return uploadedImage;
    }
}
