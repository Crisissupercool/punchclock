package ch.zli.m223.controller;

import ch.zli.m223.model.Employee;
import ch.zli.m223.service.EmployeeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeController {

    @Inject
    EmployeeService employeeService;

    @GET
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getEmployee(@PathParam("id") Long id) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(employee).build();
    }

    @POST
    public Response createEmployee(Employee employee) {
        Employee created = employeeService.createEmployee(employee);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
}

