package com.trivia.admin.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class BusinessExceptionHandlerFactory extends ExceptionHandlerFactory {
    private ExceptionHandlerFactory exceptionHandlerFactory;

    public BusinessExceptionHandlerFactory(ExceptionHandlerFactory exceptionHandlerFactory) {
        this.exceptionHandlerFactory = exceptionHandlerFactory;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new BusinessExceptionHandler(exceptionHandlerFactory.getExceptionHandler());
    }
}
