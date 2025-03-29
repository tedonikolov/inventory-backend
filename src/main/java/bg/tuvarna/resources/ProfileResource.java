package bg.tuvarna.resources;

import bg.tuvarna.models.dto.requests.CreateUserDTO;
import bg.tuvarna.models.dto.requests.LoginDTO;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.services.impl.KeycloakService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/inventory-api/v1/profile")
@ApplicationScoped
public class ProfileResource {
    @Inject
    SecurityIdentity securityIdentity;

    private final KeycloakService keycloakService;

    public ProfileResource(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @GET
    @Authenticated
    public Response getProfile() {
        return Response.ok(securityIdentity.getPrincipal().getName()).build();
    }

    @POST
    @Path("/register")
    @RolesAllowed("ADMIN")
    public Response addProfile(@RequestBody CreateUserDTO userDto) {
        keycloakService.registerUser(userDto);
        return Response.ok().build();
    }

    @Path("/login")
    @POST
    @PermitAll
    public Response loginUser(@RequestBody LoginDTO user) {
        try {
            String token = keycloakService.loginUser(user.getUsername(), user.getPassword());
            if (token == null) return Response.noContent().build();
            return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), ErrorCode.WrongCredentials);
        }
    }
}
