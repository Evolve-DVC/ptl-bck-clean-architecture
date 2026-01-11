package com.empresa.plantilla.commons.exception;

/**
 * Represents an application-specific exception.
 * This exception is used to handle application layer errors and orchestration issues.
 */
public class ApplicationException extends RuntimeException {
    
    /**
     * Constructs a new ApplicationException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public ApplicationException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new ApplicationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
