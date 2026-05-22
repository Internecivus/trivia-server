package com.trivia.admin.security.authentication;

import com.trivia.persistence.entity.User;

import javax.security.enterprise.CallerPrincipal;


public class UserCallerPrincipal extends CallerPrincipal {
    private final User user;

    public UserCallerPrincipal(User user) {
        super(user.getName());
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
