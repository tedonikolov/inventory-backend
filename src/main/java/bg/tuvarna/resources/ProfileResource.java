package bg.tuvarna.resources;

import bg.tuvarna.enums.EmployeePosition;
import bg.tuvarna.enums.ProfileRole;
import bg.tuvarna.models.dto.requests.ChangePasswordDTO;
import bg.tuvarna.models.dto.response.LoggedUser;
import bg.tuvarna.models.dto.requests.LoginDTO;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.services.EmployeeServices;
import bg.tuvarna.services.impl.KeycloakService;
import bg.tuvarna.services.impl.NotificationSenderService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.stream.Collectors;

@Path("/inventory-api/v1/profile")
@ApplicationScoped
public class ProfileResource {
    @Inject
    SecurityIdentity securityIdentity;
    @Inject
    RoutingContext context;

    private final NotificationSenderService notificationSenderService;
    private final KeycloakService keycloakService;
    private final EmployeeServices employeeServices;

    public ProfileResource(NotificationSenderService notificationSenderService, KeycloakService keycloakService, EmployeeServices employeeServices) {
        this.notificationSenderService = notificationSenderService;
        this.keycloakService = keycloakService;
        this.employeeServices = employeeServices;
    }

    @GET
    @Authenticated
    public Response getProfile() {
        LoggedUser loggedUser = new LoggedUser();
        loggedUser.setUsername(securityIdentity.getPrincipal().getName());
        loggedUser.setRoles(securityIdentity.getRoles().stream().filter(
                role -> role.equals(ProfileRole.ADMIN.name()) || role.equals(ProfileRole.CLIENT.name()) || role.equals(EmployeePosition.MOL.name()) || role.equals(EmployeePosition.WORKER.name())
        ).collect(Collectors.toSet()));

        return Response.ok(loggedUser).build();
    }

    @PUT
    @Path("/changePassword")
    @Authenticated
    public Response changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        keycloakService.changePassword(changePasswordDTO);

        return Response.ok().build();
    }

    @Path("/login")
    @POST
    @PermitAll
    public Response loginUser(@RequestBody LoginDTO user) {
        try {
            String token = keycloakService.loginUser(user.getUsername(), user.getPassword(), context);

            return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), ErrorCode.WrongCredentials);
        }
    }

    @PermitAll
    @GET
    @Path("/sendPushNotification/{id}")
    public Response test(@PathParam("id") Long id,
                         @QueryParam("title") String title,
                         @QueryParam("body") String body,
                         @QueryParam("type") String type) {
        notificationSenderService.sendPushNotification(employeeServices.findEmployeeById(id).getPhoneToken(),title,body,type,id);
        return Response.ok().build();
    }
}
