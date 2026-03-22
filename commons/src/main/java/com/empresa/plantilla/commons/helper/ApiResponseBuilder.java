package com.empresa.plantilla.commons.helper;

import com.empresa.plantilla.commons.constants.MessageKeys;
import com.empresa.plantilla.commons.dto.response.GenericResponse;
import com.empresa.plantilla.commons.services.pageable.IPageableResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Component for building standardized response objects for API communications.
 * This class provides methods to create consistent internationalized responses across the application.
 *
 * <p>Uses MessageSource for internationalization support.</p>
 *
 * @see GenericResponse
 * @see IPageableResult
 */
@Component
@RequiredArgsConstructor
public class ApiResponseBuilder {
    private static final MessageSource messageSource = null;

    /**
     * Obtiene un mensaje internacionalizado.
     *
     * @param key    la clave del mensaje
     * @param params los parámetros opcionales
     * @return el mensaje traducido
     */
    private static String getMessage(String key, Object... params) {
        try {
            return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * Creates a successful response with a single object and custom message.
     *
     * @param <T>     the type of the object
     * @param obj     the object to be included in the response
     * @param mensaje custom message
     * @return a GenericResponse containing the object
     */
    public <T> GenericResponse<T> success(T obj, String mensaje) {
        return GenericResponse.success(
                HttpStatus.OK.value(),
                mensaje,
                obj
        );
    }

    /**
     * Creates a successful response with a list of objects and custom message.
     *
     * @param <T>     the type of elements in the list
     * @param list    the list of objects to be included
     * @param mensaje custom message
     * @return a GenericResponse containing the list
     */
    public <T> GenericResponse<T> successList(List<T> list, String mensaje) {
        return GenericResponse.success(
                HttpStatus.OK.value(),
                mensaje,
                list != null ? list : Collections.emptyList()
        );
    }

    /**
     * Creates a paginated response from an IPageableResult with custom message.
     *
     * @param <T>            the type of elements in the result
     * @param pageableResult the pageable result containing items and metadata
     * @param mensaje        custom message
     * @return a GenericResponse with pagination metadata
     */
    public <T> GenericResponse<T> paginated(IPageableResult<T> pageableResult, String mensaje) {
        if (pageableResult == null || pageableResult.getTotalElements() == 0) {
            return GenericResponse.success(
                    HttpStatus.OK.value(),
                    getMessage(MessageKeys.SUCCESS_NO_RESULTS),
                    Collections.emptyList()
            );
        }

        int totalPages = (int) Math.ceil((double) pageableResult.getTotalElements() / pageableResult.getPageSize());
        int currentPage = pageableResult.getPageNumber() + 1;

        return GenericResponse.successPaginated(
                HttpStatus.OK.value(),
                mensaje,
                pageableResult.getContent(),
                pageableResult.getTotalElements().intValue(),
                getMessage(MessageKeys.SUCCESS_PAGE_INFO, currentPage, totalPages)
        );
    }

    /**
     * Creates a no content response (HTTP 204) with custom message.
     *
     * @param <T>     the type of the response
     * @param mensaje custom message
     * @return a GenericResponse with HTTP 204 status
     */
    public <T> GenericResponse<T> noContent(String mensaje) {
        return GenericResponse.success(
                HttpStatus.NO_CONTENT.value(),
                mensaje,
                (T) null
        );
    }

    /**
     * Creates an error response from an exception.
     *
     * @param <T> the type of the response
     * @param ex  the exception that caused the error
     * @return a GenericResponse with error information
     */
    public <T> GenericResponse<T> error(Throwable ex) {
        return GenericResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
    }

    /**
     * Creates an error response from an exception with custom error code.
     *
     * @param <T>       the type of the response
     * @param ex        the exception that caused the error
     * @param errorCode the HTTP error code
     * @return a GenericResponse with error information
     */
    public <T> GenericResponse<T> error(Throwable ex, Integer errorCode) {
        return GenericResponse.error(
                errorCode,
                ex.getMessage()
        );
    }

    /**
     * Creates an error response with a custom message.
     *
     * @param <T>     the type of the response
     * @param mensaje error message
     * @return a GenericResponse with error information
     */
    public <T> GenericResponse<T> error(String mensaje) {
        return GenericResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                mensaje
        );
    }

    /**
     * Creates an error response with custom error code and message.
     *
     * @param <T>       the type of the response
     * @param errorCode the HTTP error code
     * @param mensaje   error message
     * @return a GenericResponse with error information
     */
    public <T> GenericResponse<T> error(Integer errorCode, String mensaje) {
        return GenericResponse.error(errorCode, mensaje);
    }

    /**
     * Creates a bad request response (HTTP 400).
     *
     * @param <T>     the type of the response
     * @param mensaje error message
     * @return a GenericResponse with HTTP 400 status
     */
    public <T> GenericResponse<T> badRequest(String mensaje) {
        return GenericResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                mensaje
        );
    }

    /**
     * Creates a not found response (HTTP 404).
     *
     * @param <T>     the type of the response
     * @param mensaje error message
     * @return a GenericResponse with HTTP 404 status
     */
    public <T> GenericResponse<T> notFound(String mensaje) {
        return GenericResponse.error(
                HttpStatus.NOT_FOUND.value(),
                mensaje
        );
    }

    /**
     * Creates an unauthorized response (HTTP 401).
     *
     * @param <T>     the type of the response
     * @param mensaje error message
     * @return a GenericResponse with HTTP 401 status
     */
    public <T> GenericResponse<T> unauthorized(String mensaje) {
        return GenericResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                mensaje
        );
    }

    /**
     * Creates a forbidden response (HTTP 403).
     *
     * @param <T>     the type of the response
     * @param mensaje error message
     * @return a GenericResponse with HTTP 403 status
     */
    public <T> GenericResponse<T> forbidden(String mensaje) {
        return GenericResponse.error(
                HttpStatus.FORBIDDEN.value(),
                mensaje
        );
    }
}
