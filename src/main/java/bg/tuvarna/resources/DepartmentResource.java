package bg.tuvarna.resources;

import bg.tuvarna.models.dto.DepartmentDTO;
import bg.tuvarna.resources.execptions.ErrorResponse;
import bg.tuvarna.services.DepartmentService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@ApplicationScoped
@Path("/inventory-api/v1/departments")
public class DepartmentResource {
    private final DepartmentService service;

    public DepartmentResource(DepartmentService service) {
        this.service = service;
    }

    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Save or update department.",
            description = "Used to save or update department.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful saved"),
            @APIResponse(responseCode = "400", description = "Department name already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public Response save(@RequestBody DepartmentDTO dto) {
        service.save(dto);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Delete department.",
            description = "Used to delete department by id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful deleted"),
            @APIResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public Response remove(@PathParam("id") Long id) {
        service.removeDepartment(id);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Authenticated
    @Operation(summary = "Returns the department.",
            description = "Used to return department info by id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful returns",
            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentDTO.class)))
    })
    public Response getDepartment(@PathParam("id") Long id) {
        return Response.ok(service.getDepartment(id)).build();
    }

    @GET
    @RolesAllowed("ADMIN")
    @Operation(summary = "Returns the department.",
            description = "Used to return department info by id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful returns",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentDTO.class)))
    })
    public Response getAllDepartments() {
        return Response.ok(service.getAllDepartments()).build();
    }
}
