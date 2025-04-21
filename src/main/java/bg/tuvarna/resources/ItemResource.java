package bg.tuvarna.resources;

import bg.tuvarna.models.dto.ItemDTO;
import bg.tuvarna.models.dto.requests.ItemFilter;
import bg.tuvarna.models.dto.requests.ItemWithImageDTO;
import bg.tuvarna.resources.execptions.ErrorResponse;
import bg.tuvarna.services.ItemService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@ApplicationScoped
@Path("/inventory-api/v1/items")
public class ItemResource {
    private final ItemService service;

    public ItemResource(ItemService service) {
        this.service = service;
    }

    @POST
    @RolesAllowed({"ADMIN","MOL"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Save or update item.",
            description = "Used to save or update item.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful saved"),
            @APIResponse(responseCode = "400", description = "Department name already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public Response save(ItemWithImageDTO dto) {
        service.save(dto);
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
                            schema = @Schema(implementation = ItemDTO.class)))
    })
    public Response getItem(@PathParam("id") Long id) {
        return Response.ok(service.getItemById(id)).build();
    }

    @GET
    @Authenticated
    @Operation(summary = "Returns the items based on filter.",
            description = "Used to return items info by id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successfully, returns the announcements.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    type = SchemaType.OBJECT,
                                    properties = {
                                            @SchemaProperty(name = "totalPages", type = SchemaType.INTEGER),
                                            @SchemaProperty(name = "items", implementation = ItemDTO[].class),
                                            @SchemaProperty(name = "currentPage", type = SchemaType.INTEGER),
                                            @SchemaProperty(name = "pageSize", type = SchemaType.INTEGER)
                                    }
                            )))})
    public Response getAllItems(@BeanParam ItemFilter filter) {
        return Response.ok(service.getAllItems(filter)).build();
    }
}
