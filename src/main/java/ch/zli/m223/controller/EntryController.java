package ch.zli.m223.controller;

import ch.zli.m223.dto.EntryDTO;
import ch.zli.m223.model.Category;
import ch.zli.m223.model.Employee;
import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Tag;
import ch.zli.m223.service.CategoryService;
import ch.zli.m223.service.EmployeeService;
import ch.zli.m223.service.EntryService;
import ch.zli.m223.service.TagService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/entries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntryController {

    @Inject
    EntryService entryService;

    @Inject
    EmployeeService employeeService;

    @Inject
    CategoryService categoryService;

    @Inject
    TagService tagService;

    @GET
    public List<EntryDTO> getAllEntries() {
        return entryService.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response getEntry(@PathParam("id") Long id) {
        Entry entry = entryService.findById(id);
        if (entry == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(toDTO(entry)).build();
    }

    @POST
    public Response createEntry(EntryDTO entryDTO) {
        Employee employee = employeeService.findById(entryDTO.getEmployeeId());
        if (employee == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Employee not found").build();
        }

        Entry entry = new Entry();
        entry.setCheckIn(entryDTO.getCheckIn());
        entry.setCheckOut(entryDTO.getCheckOut());
        entry.setEmployee(employee);

        if (entryDTO.getCategoryId() != null) {
            Category category = categoryService.findById(entryDTO.getCategoryId());
            if (category == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Category not found").build();
            }
            entry.setCategory(category);
        }

        if (entryDTO.getTagIds() != null && !entryDTO.getTagIds().isEmpty()) {
            List<Tag> tags = entryDTO.getTagIds().stream()
                    .map(tagId -> {
                        Tag tag = tagService.findById(tagId);
                        if (tag == null) {
                            throw new IllegalArgumentException("Tag with id " + tagId + " not found");
                        }
                        return tag;
                    })
                    .collect(Collectors.toList());
            entry.setTags(tags);
        }

        Entry created = entryService.createEntry(entry);
        return Response.status(Response.Status.CREATED).entity(toDTO(created)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateEntry(@PathParam("id") Long id, EntryDTO entryDTO) {
        Employee employee = employeeService.findById(entryDTO.getEmployeeId());
        if (employee == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Employee not found").build();
        }

        Entry updatedEntry = new Entry();
        updatedEntry.setCheckIn(entryDTO.getCheckIn());
        updatedEntry.setCheckOut(entryDTO.getCheckOut());
        updatedEntry.setEmployee(employee);

        if (entryDTO.getCategoryId() != null) {
            Category category = categoryService.findById(entryDTO.getCategoryId());
            if (category == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Category not found").build();
            }
            updatedEntry.setCategory(category);
        }

        if (entryDTO.getTagIds() != null && !entryDTO.getTagIds().isEmpty()) {
            List<Tag> tags = entryDTO.getTagIds().stream()
                    .map(tagId -> {
                        Tag tag = tagService.findById(tagId);
                        if (tag == null) {
                            throw new IllegalArgumentException("Tag with id " + tagId + " not found");
                        }
                        return tag;
                    })
                    .collect(Collectors.toList());
            updatedEntry.setTags(tags);
        }

        Entry entry = entryService.updateEntry(id, updatedEntry);
        if (entry == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(toDTO(entry)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEntry(@PathParam("id") Long id) {
        entryService.deleteEntry(id);
        return Response.noContent().build();
    }

    // Hilfsmethode zum Umwandeln von Entity -> DTO
    private EntryDTO toDTO(Entry entry) {
        List<Long> tagIds = entry.getTags().stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        return new EntryDTO(
                entry.getId(),
                entry.getCheckIn(),
                entry.getCheckOut(),
                entry.getEmployee().getId(),
                entry.getCategory() != null ? entry.getCategory().getId() : null,
                tagIds
        );
    }
}
