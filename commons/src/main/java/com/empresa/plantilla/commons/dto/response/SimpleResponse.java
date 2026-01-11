package com.empresa.plantilla.commons.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents a generic response structure for architectural purposes.
 * This class is designed to encapsulate response data including a status code, 
 * a message, and a body containing a list of elements.
 *
 * @param <T> the type of elements in the response body
 * @deprecated Use {@link GenericResponse} instead which provides more flexibility and handles both simple and paginated responses.
 */
@Deprecated
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SimpleResponse<T> implements Serializable {
    /**
     * Indicates whether the operation was successful.
     */
    private boolean ok;
    /**
     * The status code of the response.
     */
    private Integer codigo;

    /**
     * A message describing the response or providing additional information.
     */
    private String mensaje;

    /**
     * The body of the response, containing a list of elements of type T.
     * This field is marked as transient to exclude it from serialization if needed.
     */
    private transient T cuerpo;
}
