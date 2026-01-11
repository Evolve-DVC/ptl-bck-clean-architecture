package com.empresa.plantilla.application.config.exception;

import com.empresa.plantilla.commons.services.i18.MessageService;
import com.empresa.plantilla.commons.constants.MessageKeys;
import com.empresa.plantilla.commons.dto.response.GenericResponse;
import com.empresa.plantilla.commons.exception.ApplicationException;
import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.commons.exception.InfrastructureException;
import com.empresa.plantilla.commons.helper.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * This class handles various types of exceptions and provides appropriate internationalized responses.
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandlerConfig extends ResponseEntityExceptionHandler  {

    private final ResponseBuilder responseBuilder;
    private final MessageService messageService;

    public ErrorHandlerConfig(@Qualifier("apiResponseBuilder") ResponseBuilder responseBuilder,
                              MessageService messageService) {
        this.responseBuilder = responseBuilder;
        this.messageService = messageService;
    }

    /**
     * Handles all uncaught exceptions.
     *
     * @param e The exception that was thrown.
     * @return A ResponseEntity containing an error response with INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<GenericResponse<Void>> all(Exception e) {
        log.error("Uncaught exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBuilder.error(e, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    /**
     * Handles ConstraintViolationException (validaciones en parámetros de métodos)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericResponse<Void>> handleConstraintViolation(ConstraintViolationException e) {
        log.error("Constraint violation: {}", e.getMessage());
        String errors = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(responseBuilder.badRequest(messageService.getMessage(MessageKeys.ERROR_CONSTRAINT_VIOLATION, errors)));
    }

    /**
     * Handles IllegalArgumentException (argumentos inválidos)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GenericResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        log.error("Illegal argument: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(responseBuilder.badRequest(messageService.getMessage(MessageKeys.ERROR_ILLEGAL_ARGUMENT, e.getMessage())));
    }

    /**
     * Handles MethodArgumentTypeMismatchException (tipo de parámetro incorrecto)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GenericResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.error("Type mismatch: {}", e.getMessage());
        String error = messageService.getMessage(MessageKeys.ERROR_TYPE_MISMATCH,
                e.getName(),
                e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");
        return ResponseEntity.badRequest()
                .body(responseBuilder.badRequest(error));
    }

    /**
     * Handles HttpMessageNotReadableException (JSON mal formado)
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        log.error("Message not readable: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(responseBuilder.badRequest(messageService.getMessage(MessageKeys.ERROR_JSON_INVALID)));
    }

    /**
     * Handles HttpRequestMethodNotSupportedException (metodo HTTP no soportado)
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        log.error("Method not supported: {}", ex.getMessage());
        String message = messageService.getMessage(MessageKeys.ERROR_METHOD_NOT_SUPPORTED,
                ex.getMethod(),
                ex.getSupportedHttpMethods());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(responseBuilder.error(HttpStatus.METHOD_NOT_ALLOWED.value(), message));
    }

    /**
     * Handles HttpMediaTypeNotSupportedException (tipo de contenido no soportado)
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        log.error("Media type not supported: {}", ex.getMessage());
        String message = messageService.getMessage(MessageKeys.ERROR_MEDIA_TYPE_NOT_SUPPORTED, ex.getContentType());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(responseBuilder.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), message));
    }

    /**
     * Handles MissingServletRequestParameterException (parámetro requerido faltante)
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        log.error("Missing parameter: {}", ex.getMessage());
        String message = messageService.getMessage(MessageKeys.ERROR_PARAMETER_MISSING, ex.getParameterName());
        return ResponseEntity.badRequest()
                .body(responseBuilder.badRequest(message));
    }

    /**
     * Handles NoHandlerFoundException (endpoint no encontrado)
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        log.error("No handler found: {}", ex.getMessage());
        String message = messageService.getMessage(MessageKeys.ERROR_ENDPOINT_NOT_FOUND, ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseBuilder.notFound(message));
    }


    /**
     * Handles NullPointerException (referencia nula)
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<GenericResponse<Void>> handleNullPointer(NullPointerException e) {
        log.error("Null pointer exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        messageService.getMessage(MessageKeys.ERROR_NULL_POINTER)));
    }
    
    /**
     * Handles ApplicationExceptions.
     *
     * @param e The ApplicationException that was thrown.
     * @return A ResponseEntity containing an error response with INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<GenericResponse<Void>> applicationException(ApplicationException e) {
        log.error("Application exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBuilder.error(e, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    /**
     * Handles DomainExceptions.
     *
     * @param e The DomainException that was thrown.
     * @return A ResponseEntity containing an error response with BAD_REQUEST status.
     */
    @ExceptionHandler(DomainException.class)
    protected ResponseEntity<GenericResponse<Void>> domainException(DomainException e) {
        log.error("Domain exception: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(responseBuilder.badRequest(e.getMessage()));
    }

    /**
     * Handles InfrastructureExceptions.
     *
     * @param e The InfrastructureException that was thrown.
     * @return A ResponseEntity containing an error response with INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(InfrastructureException.class)
    protected ResponseEntity<GenericResponse<Void>> infrastructureException(InfrastructureException e) {
        log.error("Infrastructure exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBuilder.error(e, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    /**
     * Handles MethodArgumentNotValidException which occurs when @Valid fails.
     *
     * @param ex The MethodArgumentNotValidException that was thrown.
     * @param headers The headers to be written to the response.
     * @param status The selected response status.
     * @param request The current request.
     * @return A ResponseEntity containing an error response with details of validation failures.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        log.error("Validation failed: {}", ex.getMessage());

        StringBuilder errorMessage = new StringBuilder(messageService.getMessage(MessageKeys.ERROR_VALIDATION_PREFIX) + ": ");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            errorMessage.append(message).append("; ");
        });

        // Remover el último "; "
        if (errorMessage.length() > messageService.getMessage(MessageKeys.ERROR_VALIDATION_PREFIX).length() + 2) {
            errorMessage.setLength(errorMessage.length() - 2);
        }

        return ResponseEntity.badRequest()
                .body(responseBuilder.badRequest(errorMessage.toString()));
    }

    /**
     * Handles DataIntegrityViolationException which can occur due to various database constraint violations,
     * including foreign key constraints.
     *
     * @param e The DataIntegrityViolationException that was thrown.
     * @return A ResponseEntity containing an error response with BAD_REQUEST status.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<GenericResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("Data integrity violation: {}", e.getMessage());

        String userFriendlyMessage = messageService.getMessage(MessageKeys.ERROR_DATA_INTEGRITY);
        if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
            userFriendlyMessage = messageService.getMessage(MessageKeys.ERROR_FK_CONSTRAINT);
        }

        return ResponseEntity.badRequest()
                .body(responseBuilder.badRequest(userFriendlyMessage));
    }
}
