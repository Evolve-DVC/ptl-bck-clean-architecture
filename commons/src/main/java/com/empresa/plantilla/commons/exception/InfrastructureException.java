package com.empresa.plantilla.commons.exception;


/**
 * Custom exception class for infrastructure-related errors.
 * This exception extends RuntimeException and provides various constructors
 * for different error scenarios.
 */
public class InfrastructureException extends RuntimeException {

    /**
     * Constructs a new InfrastructureException with no specified detail message or cause.
     */
    public InfrastructureException() {
        super();
    }

    /**
     * Constructs a new InfrastructureException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public InfrastructureException(String message) {
        super(message);
    }

    /**
     * Constructs a new InfrastructureException with the specified cause.
     *
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public InfrastructureException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new InfrastructureException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause   the cause (which is saved for later retrieval by the getCause() method)
     */
    public InfrastructureException(String message, Throwable cause) {
        super(message, cause);
    }
}
