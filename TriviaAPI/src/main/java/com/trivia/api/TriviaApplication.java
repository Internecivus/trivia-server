package com.trivia.api;

import com.trivia.api.api.v1.CategoriesEndpoint;
import com.trivia.api.api.v1.ClientEndpoint;
import com.trivia.api.api.v1.QuestionsEndpoint;
import com.trivia.api.exception.BusinessExceptionMapper;
import com.trivia.api.exception.ConstraintViolationExceptionMapper;
import com.trivia.api.json.JacksonContextResolver;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("") // application.xml path has priority.
public class TriviaApplication extends Application {
    private HashSet<Class<?>> classes = new HashSet<>();
    private Set<Object> singletons = new HashSet<>();

    public TriviaApplication() {
        classes.add(QuestionsEndpoint.class);
        classes.add(CategoriesEndpoint.class);
        classes.add(ClientEndpoint.class);
        classes.add(BusinessExceptionMapper.class);
        classes.add(ConstraintViolationExceptionMapper.class);
        classes.add(JacksonContextResolver.class);
    }

    @Override
    public HashSet<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}