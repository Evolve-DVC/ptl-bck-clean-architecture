package com.empresa.plantilla.commons.exception;

/**
 * Custom exception class for domain-specific errors.
 * This exception represents violations of business rules or domain invariants.
 */
public class DomainException extends RuntimeException {
    
    /**
     * Constructs a new DomainException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public DomainException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new DomainException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}