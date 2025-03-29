package bg.tuvarna.resources.execptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception handler for CustomException.
 * This class maps CustomException to appropriate HTTP responses.
 */
@Provider
public class CustomExceptionHandler implements ExceptionMapper<CustomException> {

    /**
     * Converts a CustomException to an HTTP response.
     *
     * @param exception the CustomException to convert
     * @return a Response object representing the HTTP response
     */
    @Override
    public Response toResponse(CustomException exception) {
        if (exception.getErrorCode() == ErrorCode.EntityNotFound) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(exception)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(exception)).build();
        }
    }
}
