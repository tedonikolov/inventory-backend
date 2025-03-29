package bg.tuvarna.resources.execptions;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing various error codes used in the application.
 */
public enum ErrorCode {
    /**
     * Error code indicating that an entity was not found.
     */
    EntityNotFound(0),

    /**
     * Error code indicating that an entity already exists.
     */
    AlreadyExists(1),

    /**
     * Error code indicating that the provided credentials are wrong.
     */
    WrongCredentials(2),

    /**
     * Error code indicating that the entity is not registered.
     */
    NotRegistered(3),

    /**
     * Error code indicating a general failure.
     */
    Failed(4);

    private final int code;

    /**
     * Constructs an ErrorCode with the specified code.
     *
     * @param code the integer code representing the error
     */
    ErrorCode(int code) {
        this.code = code;
    }

    /**
     * Returns the integer code associated with this error code.
     *
     * @return the integer code
     */
    @JsonValue // to return label in Json responses
    public int getCode() {
        return code;
    }
}
