package ch.zli.m223.controller;

import ch.zli.m223.model.Entry;
import ch.zli.m223.service.EntryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/entries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntryController {

    @Inject
    EntryService entryService;

    @GET
    public List<Entry> getAllEntries() {
        return entryService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getEntry(@PathParam("id") Long id) {
        Entry entry = entryService.findById(id);
        if (entry == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entry).build();
    }

    @POST
    public Response createEntry(Entry entry) {
        try {
            Entry created = entryService.createEntry(entry);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateEntry(@PathParam("id") Long id, Entry updatedEntry) {
        Entry entry = entryService.updateEntry(id, updatedEntry);
        if (entry == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entry).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEntry(@PathParam("id") Long id) {
        entryService.deleteEntry(id);
        return Response.noContent().build();
    }
}
