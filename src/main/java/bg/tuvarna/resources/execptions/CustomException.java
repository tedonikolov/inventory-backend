package bg.tuvarna.resources.execptions;

/**
 * Custom exception class for handling specific error codes.
 */
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    /**
     * Constructs a new CustomException with the specified detail message and error code.
     *
     * @param message the detail message
     * @param errorCode the error code associated with this exception
     */
    public CustomException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}