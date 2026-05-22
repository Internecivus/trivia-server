package com.trivia.admin.controller.user;

import com.trivia.core.service.UserService;
import com.trivia.persistence.EntityView;
import com.trivia.persistence.entity.User;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@ViewScoped
public class UserDetailsController implements Serializable {
    private @Inject UserService userService;
    private transient @Inject FacesContext facesContext;
    private User user;

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        this.user = userService.findById(id, EntityView.UserDetails);
    }

    public User getUser() {
        return user;
    }
}