package bg.tuvarna.resources;

import bg.tuvarna.models.dto.EmployeeDTO;
import bg.tuvarna.models.dto.requests.CreateUserDTO;
import bg.tuvarna.models.dto.requests.EmployeeWithImageDTO;
import bg.tuvarna.resources.execptions.ErrorResponse;
import bg.tuvarna.services.EmployeeServices;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@ApplicationScoped
@Path("/inventory-api/v1/employees")
public class EmployeeResource {
    private final EmployeeServices service;

    public EmployeeResource(EmployeeServices service) {
        this.service = service;
    }

    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Save or update employee.",
            description = "Used to save or update employee.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful saved"),
            @APIResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public Response save(@RequestBody CreateUserDTO dto) {
        service.save(dto);
        return Response.ok().build();
    }

    @PUT
    @Authenticated
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Update employee.",
            description = "Used to update employee by id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful deleted"),
            @APIResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public Response update(EmployeeWithImageDTO employeeWithImageDTO) {
        service.update(employeeWithImageDTO);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Authenticated
    @Operation(summary = "Returns the employee.",
            description = "Used to return employee info by id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful returns",
            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class)))
    })
    public Response getEmployee(@PathParam("id") Long id) {
        return Response.ok(service.getEmployeeById(id)).build();
    }

    @GET
    @Path("/all")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Returns the employees.",
            description = "Used to return employees.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful returns",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO[].class)))
    })
    public Response getAllEmployees() {
        return Response.ok(service.getAllEmployees()).build();
    }

    @GET
    @Authenticated
    @Operation(summary = "Returns the employee.",
            description = "Used to return employee info by id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful returns",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class)))
    })
    public Response getEmployeeByUsername(@QueryParam("name") String name) {
        return Response.ok(service.getEmployeeByUsername(name)).build();
    }
}
