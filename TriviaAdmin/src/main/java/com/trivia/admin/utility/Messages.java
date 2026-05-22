package com.trivia.admin.utility;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

// FIXME: For some reason the locale change is not recognised here
// This is basically stolen from Omnifaces :-)
// The only reason why we are using this instead of Omnifaces is that we want both details and the summary
// of the messages without all the fussy configuration.
public final class Messages {
    private static FacesMessage facesMessage;

    private Messages() {}

    public static void addErrorGlobalFlash(String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, message);
        addGlobalFlash();
    }

    public static void addFatalGlobalFlash(String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, message);
        addGlobalFlash();
    }

    public static void addInfoGlobalFlash(String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, message);
        addGlobalFlash();
    }

    public static void addWarnGlobalFlash(String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, message);
        addGlobalFlash();
    }

    public static void addErrorGlobalFlashFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, message);
        addFlash();
        addFor(clientId);
    }

    public static void addFatalGlobalFlashFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, message);
        addFlash();
        addFor(clientId);
    }

    public static void addInfoGlobalFlashFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, message);
        addFlash();
        addFor(clientId);
    }

    public static void addWarnGlobalFlashFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, message);
        addFlash();
        addFor(clientId);
    }

    public static void addErrorGlobal(String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, message);
        addGlobal();
    }

    public static void addInfoGlobal(String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, message);
        addGlobal();
    }

    public static void addWarnGlobal(String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, message);
        addGlobal();
    }

    public static void addFatalGlobal(String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, message);
        addGlobal();
    }

    public static void addInfoFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, message);
        addFor(clientId);
    }

    public static void addWarnFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, message);
        addFor(clientId);
    }

    public static void addErrorFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, message);
        addFor(clientId);
    }

    public static void addFatalFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, message);
        addFor(clientId);
    }

    public static void addInfoFlashFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, message);
        addFlash();
        addFor(clientId);
    }

    public static void addWarnFlashFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, message);
        addFlash();
        addFor(clientId);
    }

    public static void addErrorFlashFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, message);
        addFlash();
        addFor(clientId);
    }

    public static void addFatalFlashFor(String clientId, String summary, String message) {
        facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, message);
        addFlash();
        addFor(clientId);
    }

    private static void addGlobalFlash() {
        addFlash();
        addGlobal();
    }

    private static void addGlobal() {
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    private static void addFor(String clientId) {
        FacesContext.getCurrentInstance().addMessage(clientId, facesMessage);
    }

    private static void addFlash() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
}