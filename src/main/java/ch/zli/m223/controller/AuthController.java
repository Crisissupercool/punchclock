package ch.zli.m223.controller;

import ch.zli.m223.dto.LoginRequestDTO;
import ch.zli.m223.dto.LoginResponseDTO;
import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.service.ApplicationUserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    ApplicationUserService applicationUserService;

    /**
     * POST /auth/login
     * Authenticate user and return JWT token
     */
    @POST
    @Path("/login")
    public Response login(LoginRequestDTO loginRequest) {
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username and password are required")
                    .build();
        }

        String token = applicationUserService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        if (token == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid username or password")
                    .build();
        }

        // Get user info for response
        Optional<ApplicationUser> userOptional = applicationUserService.findByUsername(loginRequest.getUsername());
        if (userOptional.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        ApplicationUser user = userOptional.get();
        LoginResponseDTO response = new LoginResponseDTO(token, user.getUsername(), user.getRole());

        return Response.ok(response).build();
    }

    /**
     * GET /auth/me
     * Get current user info (requires authentication)
     */
    @GET
    @Path("/me")
    public Response getCurrentUser() {
        // This endpoint can be used to verify token validity
        return Response.ok("Token is valid").build();
    }
}
