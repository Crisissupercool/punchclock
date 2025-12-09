package ch.zli.m223.controller;

import ch.zli.m223.service.TestDataService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/api/testdata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestDataController {

    @Inject
    TestDataService testDataService;

    /**
     * Manually load test data.
     * This endpoint allows you to reload test data at any time.
     *
     * Usage: POST http://localhost:8080/api/testdata
     */
    @POST
    public Response loadTestData() {
        try {
            testDataService.loadTestData();
            return Response.ok(Map.of(
                "message", "Test data loaded successfully",
                "status", "success"
            )).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                    "message", "Failed to load test data: " + e.getMessage(),
                    "status", "error"
                )).build();
        }
    }

    /**
     * Clear all test data.
     * WARNING: This will delete ALL data from the database!
     *
     * Usage: DELETE http://localhost:8080/api/testdata
     */
    @DELETE
    public Response clearTestData() {
        try {
            testDataService.clearAllData();
            return Response.ok(Map.of(
                "message", "All data cleared successfully",
                "status", "success"
            )).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                    "message", "Failed to clear data: " + e.getMessage(),
                    "status", "error"
                )).build();
        }
    }
}
