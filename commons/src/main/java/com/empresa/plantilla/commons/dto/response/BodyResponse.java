package com.empresa.plantilla.commons.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a generic response body for API endpoints.
 * This class encapsulates count, data, and total information in a standardized format.
 *
 * @param <T> the type of elements in the data list
 * @deprecated Use {@link GenericResponse} instead which now handles both simple and paginated responses.
 */
@Deprecated
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public final class BodyResponse<T> implements Serializable {
    /**
     * The count of items in the response.
     */
    private Integer conteo;

    /**
     * The list of data items. This field is marked as transient.
     */
    private transient List<T> datos;

    /**
     * A string representation of totals or summary information.
     */
    private String totales;
}
