package com.trivia.admin.controller.question;

import com.trivia.core.service.QuestionService;
import com.trivia.core.utility.ImageUtil;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.Question;

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
public class QuestionDetailsController implements Serializable {
    private @Inject QuestionService questionService;
    private transient @Inject FacesContext facesContext;
    private Question question;
    private byte[] uploadedImage;

    @PostConstruct
    public void init() throws IOException {
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        this.question = questionService.findById(id, EntityView.QuestionDetails);

        if (question.getImage() != null) {
            uploadedImage = ImageUtil.getImage(question.getImage());
        }
    }

    public Question getQuestion() {
        return question;
    }

    public byte[] getUploadedImage() {
        return uploadedImage;
    }
}
