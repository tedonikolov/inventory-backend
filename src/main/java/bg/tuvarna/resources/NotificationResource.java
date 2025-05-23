package bg.tuvarna.resources;

import bg.tuvarna.models.dto.NotificationDTO;
import bg.tuvarna.services.NotificationService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/inventory-api/v1/notifications")
public class NotificationResource {
    private final NotificationService notificationService;

    public NotificationResource(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @POST
    @Path("/sendNotification")
    @RolesAllowed({"ADMIN", "MOL"})
    public Response sendNotification(@RequestBody NotificationDTO notificationDTO) {
        notificationService.createNotify(notificationDTO);
        return Response.ok().build();
    }

    @POST
    @Path("/sendNotificationForAll")
    @RolesAllowed({"ADMIN", "MOL"})
    public Response sendNotificationForAll(@RequestBody NotificationDTO notificationDTO) {
        notificationService.createNotifyForAll(notificationDTO);
        return Response.ok().build();
    }

    @POST
    @Path("/sendNotificationForDepartment/{id}")
    @RolesAllowed({"ADMIN", "MOL"})
    public Response sendNotificationForDepartment(@PathParam("id") Long id, @RequestBody NotificationDTO notificationDTO) {
        notificationService.createNotifyForDepartment(id, notificationDTO);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"WORKER", "MOL"})
    public Response getMyNotifications(@PathParam("id") Long id) {
        return Response.ok(notificationService.selfNotification(id)).build();
    }

    @GET
    @Path("/allForDepartment")
    @RolesAllowed({"MOL"})
    public Response getAllForDepartment(@QueryParam("departmentId") Long departmentId) {
        return Response.ok(notificationService.getAllForDepartment(departmentId)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"WORKER", "MOL"})
    public Response markAsRead(@PathParam("id") Long id) {
        notificationService.markRead(id);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"WORKER", "MOL"})
    public Response deleteNotification(@PathParam("id") Long id) {
        notificationService.deleteNotification(id);
        return Response.ok().build();
    }
}