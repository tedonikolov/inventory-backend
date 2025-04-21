package bg.tuvarna.resources;

import bg.tuvarna.models.dto.CardDTO;
import bg.tuvarna.models.dto.ItemDTO;
import bg.tuvarna.models.dto.requests.AddItemDTO;
import bg.tuvarna.models.dto.requests.CardFilter;
import bg.tuvarna.resources.execptions.ErrorResponse;
import bg.tuvarna.services.CardService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@ApplicationScoped
@Path("/inventory-api/v1/cards")
public class CardResource {
    private final CardService service;

    public CardResource(CardService service) {
        this.service = service;
    }

    @POST
    @RolesAllowed({"ADMIN","MOL"})
    @Operation(summary = "Add item for employee.",
            description = "Used to add item for employee.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful saved"),
            @APIResponse(responseCode = "400", description = "Item already in use!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @APIResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))})
    public Response save(@RequestBody AddItemDTO dto) {
        service.addItemToCard(dto);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{cardId}")
    @RolesAllowed({"ADMIN","MOL"})
    @Operation(summary = "Removes the item from the card.",
            description = "Used to return the item from the card by id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful returns",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemDTO.class)))
    })
    public Response returnItem(@PathParam("cardId") Long cardId) {
        service.returnItem(cardId);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Authenticated
    @Operation(summary = "Returns the card.",
            description = "Used to return card info by id.")
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
                                            @SchemaProperty(name = "items", implementation = CardDTO[].class),
                                            @SchemaProperty(name = "currentPage", type = SchemaType.INTEGER),
                                            @SchemaProperty(name = "pageSize", type = SchemaType.INTEGER)
                                    }
                            )))})
    public Response getAllItems(@BeanParam CardFilter filter) {
        return Response.ok(service.getAllCards(filter)).build();
    }
}
