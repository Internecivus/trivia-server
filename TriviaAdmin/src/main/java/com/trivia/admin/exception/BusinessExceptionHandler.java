package com.trivia.admin.exception;

import com.trivia.admin.resources.i18n;
import com.trivia.admin.utility.Messages;
import com.trivia.core.exception.*;

import javax.ejb.EJBAccessException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

/**
 * To my knowledge, there isn't a better way to map application exceptions to faces messages.
 * One flaw of this design is that JSF actually wraps all the exceptions in FacesException/ELException, but thank god a method
 * is already provided to "peel them off".
 */

public class BusinessExceptionHandler extends ExceptionHandlerWrapper {
    private ExceptionHandler exceptionHandler;

    public BusinessExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return exceptionHandler;
    }

    @Override
    public void handle() {
        final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable original = context.getException();
            Throwable rootCause = exceptionHandler.getRootCause(original);

            if (rootCause instanceof javax.faces.el.EvaluationException) {
                rootCause = rootCause.getCause();
            }

            // Meaning it is a FacesException/ELException.
            if (rootCause == null) {
                continue;
            }

            if (rootCause instanceof EntityNotFoundException) {
                Messages.addErrorFor("growl", i18n.get("failure"), i18n.get("notFound"));
            }
            else if (rootCause instanceof EntityExistsException) {
                Messages.addErrorFor("growl", i18n.get("failure"), i18n.get("alreadyExists"));
            }
            else if (rootCause instanceof EJBAccessException ||
                     rootCause instanceof NotAuthorizedException) {
                Messages.addErrorFor("growl", i18n.get("failure"), i18n.get("access.failure"));
            }
            else if (rootCause instanceof InvalidInputException) {
                Messages.addErrorFor("growl", i18n.get("failure"), rootCause.getMessage());
            }
            else if (rootCause instanceof InvalidCredentialException) {
                Messages.addErrorFor("growl", i18n.get("failure"),i18n.get("login.failure"));
            }
            else if (rootCause instanceof SystemException) {
                Messages.addFatalFor("growl", i18n.get("failure"), i18n.get("server.failure"));
            }
            else if (rootCause instanceof BusinessException) {
                Messages.addErrorFor("growl", i18n.get("failure"), i18n.get("service.failure"));
            }
            else {
                continue;
            }

            iterator.remove();
        }

        getWrapped().handle();
    }
}