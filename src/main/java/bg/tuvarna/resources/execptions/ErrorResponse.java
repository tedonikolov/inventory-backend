package bg.tuvarna.resources.execptions;

import java.time.LocalDateTime;

/**
 * A class representing an error response.
 * This class is used to encapsulate error details in a structured format.
 */
public final class ErrorResponse {
    private String message;
    private int errorCode;
    private LocalDateTime timestamp;

    /**
     * Constructs an ErrorResponse with the specified error message, error code, and timestamp.
     *
     * @param message the error message
     * @param errorCode the error code
     * @param timestamp the timestamp when the error occurred
     */
    public ErrorResponse(String message, int errorCode, LocalDateTime timestamp) {
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    /**
     * Constructs an ErrorResponse from a CustomException.
     *
     * @param ex the CustomException to extract error details from
     */
    public ErrorResponse(CustomException ex) {
        this(ex.getMessage(), ex.getErrorCode().getCode(), LocalDateTime.now());
    }

    /**
     * Returns the error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message.
     *
     * @param message the error message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the error code.
     *
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code.
     *
     * @param errorCode the error code to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Returns the timestamp when the error occurred.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the error occurred.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}