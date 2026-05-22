package com.trivia.api.api.v1;

import com.trivia.core.service.CategoryService;
import com.trivia.persistence.dto.client.CategoryClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;



@Path("/categories")
public class CategoriesEndpoint {
    private @Inject CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories() {
        List<CategoryClient> categories = new ArrayList<>(categoryService.toDto(categoryService.getAll()));
        return Response.status(Response.Status.OK).entity(categories).build();
    }
}