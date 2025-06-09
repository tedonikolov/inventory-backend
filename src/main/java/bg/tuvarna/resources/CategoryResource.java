package bg.tuvarna.resources;

import bg.tuvarna.models.dto.CategoryDTO;
import bg.tuvarna.models.dto.DepartmentDTO;
import bg.tuvarna.resources.execptions.ErrorResponse;
import bg.tuvarna.services.CategoryService;
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
@Path("/inventory-api/v1/categories")
public class CategoryResource {
    private final CategoryService service;

    public CategoryResource(CategoryService service) {
        this.service = service;
    }

    @POST
    @RolesAllowed("ADMIN")
    @Operation(summary = "Save or update category.",
            description = "Used to save or update category.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful saved"),
            @APIResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public Response save(@RequestBody CategoryDTO dto) {
        service.save(dto);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Authenticated
    @Operation(summary = "Returns the category.",
            description = "Used to return category info by id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful returns",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentDTO.class)))
    })
    public Response getCategory(@PathParam("id") Long id) {
        return Response.ok(service.getCategoryById(id)).build();
    }

    @GET
    @Authenticated
    @Operation(summary = "Returns the categories.",
            description = "Used to return categories in system.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful returns",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DepartmentDTO.class)))
    })
    public Response getAllCategories() {
        return Response.ok(service.getAllCategories()).build();
    }
}
