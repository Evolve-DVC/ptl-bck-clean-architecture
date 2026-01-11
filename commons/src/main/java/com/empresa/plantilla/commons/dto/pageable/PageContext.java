package com.empresa.plantilla.commons.dto.pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a context for paginated data.
 * This class is used to encapsulate pagination information along with the actual data.
 *
 * @param <T> the type of data being paginated
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PageContext<T> {

    /**
     * The actual data content of the page
     */
    private T data;

    /**
     * The current page number (zero-based)
     */
    private Integer pageNumber;

    /**
     * The size of the page (number of items per page)
     */
    private Integer pageSize;

    /**
     * The field name to sort by
     */
    private String sortBy;

    /**
     * The sort direction ("asc" or "desc")
     */
    private String sortDir;

    /**
     * The type of filter applied to the data
     */
    private String filterType;
}
