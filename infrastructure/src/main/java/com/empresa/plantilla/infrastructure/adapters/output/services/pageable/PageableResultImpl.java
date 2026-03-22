package com.empresa.plantilla.infrastructure.adapters.output.services.pageable;

import com.empresa.plantilla.commons.services.pageable.IPageableResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Implementation of the IPageableResult interface that wraps a Spring Data Page object
 * or can be constructed directly with pagination data.
 *
 * @param <M> the type of the elements in the page
 */
public class PageableResultImpl<M> implements IPageableResult<M> {

    private final List<M> content;
    private final int pageNumber;
    private final int pageSize;
    private final Long totalElements;

    /**
     * Constructor from Spring Data Page.
     *
     * @param page The Spring Data Page object
     */
    public PageableResultImpl(Page<M> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
    }

    /**
     * Constructor with direct values.
     * Useful for creating pageable results without Spring Data Page.
     *
     * @param content       The list of elements
     * @param pageNumber    The current page number (zero-based)
     * @param pageSize      The size of the page
     * @param totalElements The total number of elements
     */
    public PageableResultImpl(List<M> content, int pageNumber, int pageSize, Long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
    }

    /**
     * Retrieves the content of the current page.
     *
     * @return a List containing the elements of the current page
     */
    @Override
    public List<M> getContent() {
        return content;
    }

    /**
     * Gets the current page number.
     *
     * @return the zero-based page number of the current page
     */
    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Gets the size of the current page.
     *
     * @return the amount elements in the current page
     */
    @Override
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Gets the total amount elements across all pages.
     *
     * @return the total amount elements
     */
    @Override
    public Long getTotalElements() {
        return totalElements;
    }

    /**
     * Gets the total number of pages.
     *
     * @return the total number of pages
     */
    @Override
    public int getTotalPages() {
        if (pageSize <= 0 || totalElements == null || totalElements == 0L) {
            return 0;
        }
        return (int) Math.ceil((double) totalElements / pageSize);
    }
}
