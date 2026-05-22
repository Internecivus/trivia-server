package com.trivia.api.api.v1;

import com.trivia.core.service.QuestionService;
import com.trivia.core.utility.SortOrder;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;



@Path("/questions")
public class QuestionsEndpoint {
    private @Inject QuestionService questionService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestions(
            @MatrixParam("category") String category,
            @MatrixParam("sortField") String sortField,
            @QueryParam("page") int pageCurrent,
            @QueryParam("size") int pageSize,
            @QueryParam("sortOrder") SortOrder sortOrder,
            @QueryParam("search") String searchString,
            @DefaultValue("false") @QueryParam("random") boolean random
    ) {
        List<?> questions;

        if (random) {
            questions = questionService.toDto(questionService.getRandomFromCategory(pageSize, category));
        }
        else {
            // TODO: Only for testing!
            questions = questionService.findAll(pageCurrent, pageSize, sortField, sortOrder, searchString);
        }

        return Response.status(Response.Status.OK).entity(questions).build();
    }
}