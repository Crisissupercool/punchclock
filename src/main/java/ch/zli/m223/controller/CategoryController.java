package ch.zli.m223.controller;

import ch.zli.m223.model.Category;
import ch.zli.m223.service.CategoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryController {

    @Inject
    CategoryService categoryService;

    @GET
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getCategory(@PathParam("id") Long id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(category).build();
    }

    @POST
    public Response createCategory(Category category) {
        Category created = categoryService.createCategory(category);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        categoryService.deleteCategory(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") Long id, Category updatedCategory) {
        Category category = categoryService.updateCategory(id, updatedCategory);
        if (category == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(category).build();
    }
}
