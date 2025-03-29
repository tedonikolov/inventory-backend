package bg.tuvarna.resources.execptions;

import java.time.LocalDateTime;

/**
 * A class representing an error response.
 * This class is used to encapsulate error details in a structured format.
 */
public final class ErrorResponse {
    private String error;
    private int errorCode;
    private LocalDateTime timestamp;

    /**
     * Constructs an ErrorResponse with the specified error message, error code, and timestamp.
     *
     * @param error the error message
     * @param errorCode the error code
     * @param timestamp the timestamp when the error occurred
     */
    public ErrorResponse(String error, int errorCode, LocalDateTime timestamp) {
        this.error = error;
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
    public String getError() {
        return error;
    }

    /**
     * Sets the error message.
     *
     * @param error the error message to set
     */
    public void setError(String error) {
        this.error = error;
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