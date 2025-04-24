package bg.tuvarna.resources;

import bg.tuvarna.services.S3Service;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/inventory-api/v1/content")
public class ContentResource {
    @Inject
    S3Service s3service;

    @GET
    @Path("/department/{fileName}")
    @PermitAll
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getDepartment(@PathParam("fileName") String fileName) {
        return Response.ok(s3service.getFile("department-images", fileName)).build();
    }

    @GET
    @Path("/employee/{fileName}")
    @PermitAll
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getEmployee(@PathParam("fileName") String fileName) {
        return Response.ok(s3service.getFile("employee-images", fileName)).build();
    }

    @GET
    @Path("/item/{fileName}")
    @PermitAll
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getItem(@PathParam("fileName") String fileName) {
        return Response.ok(s3service.getFile("item-images", fileName)).build();
    }
}