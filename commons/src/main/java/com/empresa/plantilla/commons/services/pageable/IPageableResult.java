package com.empresa.plantilla.commons.services.pageable;

import java.util.List;

/**
 * Represents a pageable result containing a list of elements and pagination information.
 *
 * @param <T> the type of elements in the content list
 */
public interface IPageableResult<T> {
    /**
     * Retrieves the list of elements in the current page.
     *
     * @return a list of elements of type T
     */
    List<T> getContent();

    /**
     * Gets the current page number.
     *
     * @return the current page number (zero-based)
     */
    int getPageNumber();

    /**
     * Gets the size of the page.
     *
     * @return the number of elements per page
     */
    int getPageSize();

    /**
     * Gets the total number of elements across all pages.
     *
     * @return the total number of elements
     */
    Long getTotalElements();

    /**
     * Gets the total number of pages.
     *
     * @return the total number of pages
     */
    int getTotalPages();
}
