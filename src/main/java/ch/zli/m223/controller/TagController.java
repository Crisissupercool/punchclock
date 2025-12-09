package ch.zli.m223.controller;

import ch.zli.m223.model.Tag;
import ch.zli.m223.service.TagService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TagController {

    @Inject
    TagService tagService;

    @GET
    public List<Tag> getAllTags() {
        return tagService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getTag(@PathParam("id") Long id) {
        Tag tag = tagService.findById(id);
        if (tag == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(tag).build();
    }

    @POST
    public Response createTag(Tag tag) {
        Tag created = tagService.createTag(tag);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTag(@PathParam("id") Long id) {
        tagService.deleteTag(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTag(@PathParam("id") Long id, Tag updatedTag) {
        Tag tag = tagService.updateTag(id, updatedTag);
        if (tag == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(tag).build();
    }
}
