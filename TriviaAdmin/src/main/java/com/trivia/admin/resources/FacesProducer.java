package com.trivia.admin.resources;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;



public class FacesProducer {
    @RequestScoped
    @Produces
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
