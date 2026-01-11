package com.empresa.plantilla.commons.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a unified generic response object for API communications.
 * This class can handle both simple responses (single object) and paginated responses (list with metadata).
 *
 * @param <T> The type of data contained in the response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> implements Serializable {

    /**
     * Indicates whether the operation was successful.
     */
    private boolean ok;

    /**
     * The status code of the response (HTTP status code).
     */
    private Integer codigo;

    /**
     * A message providing additional information about the response.
     */
    private String mensaje;

    /**
     * Single data object for simple responses.
     * Use this for single entity responses.
     */
    private T dato;

    /**
     * List of data items for collection responses.
     * Use this for paginated or list responses.
     */
    private List<T> datos;

    /**
     * The count of items in the response (for paginated responses).
     */
    private Integer conteo;

    /**
     * A string representation of totals or summary information (for paginated responses).
     */
    private String totales;

    /**
     * Creates a successful response with a single data object.
     *
     * @param codigo HTTP status code
     * @param mensaje Response message
     * @param dato The data object
     * @param <T> Type of data
     * @return GenericResponse instance
     */
    public static <T> GenericResponse<T> success(Integer codigo, String mensaje, T dato) {
        return GenericResponse.<T>builder()
                .ok(true)
                .codigo(codigo)
                .mensaje(mensaje)
                .dato(dato)
                .build();
    }

    /**
     * Creates a successful response with a list of data.
     *
     * @param codigo HTTP status code
     * @param mensaje Response message
     * @param datos List of data items
     * @param <T> Type of data
     * @return GenericResponse instance
     */
    public static <T> GenericResponse<T> success(Integer codigo, String mensaje, List<T> datos) {
        return GenericResponse.<T>builder()
                .ok(true)
                .codigo(codigo)
                .mensaje(mensaje)
                .datos(datos)
                .conteo(datos != null ? datos.size() : 0)
                .build();
    }

    /**
     * Creates a successful paginated response with metadata.
     *
     * @param codigo HTTP status code
     * @param mensaje Response message
     * @param datos List of data items
     * @param conteo Total count
     * @param totales Summary information
     * @param <T> Type of data
     * @return GenericResponse instance
     */
    public static <T> GenericResponse<T> successPaginated(
            Integer codigo,
            String mensaje,
            List<T> datos,
            Integer conteo,
            String totales) {
        return GenericResponse.<T>builder()
                .ok(true)
                .codigo(codigo)
                .mensaje(mensaje)
                .datos(datos)
                .conteo(conteo)
                .totales(totales)
                .build();
    }

    /**
     * Creates an error response.
     *
     * @param codigo HTTP status code
     * @param mensaje Error message
     * @param <T> Type of data
     * @return GenericResponse instance
     */
    public static <T> GenericResponse<T> error(Integer codigo, String mensaje) {
        return GenericResponse.<T>builder()
                .ok(false)
                .codigo(codigo)
                .mensaje(mensaje)
                .build();
    }
}
