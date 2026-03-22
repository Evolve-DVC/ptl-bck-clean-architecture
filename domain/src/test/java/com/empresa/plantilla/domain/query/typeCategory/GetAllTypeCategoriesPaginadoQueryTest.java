package com.empresa.plantilla.domain.query.typeCategory;

import com.empresa.plantilla.commons.dto.pageable.PageContext;
import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.commons.services.pageable.IPageableResult;
import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.domain.query.QueryTestAbstract;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class GetAllTypeCategoriesPaginadoQueryTest extends QueryTestAbstract<PageContext<TypeCategory>, IPageableResult<TypeCategory>, GetAllTypeCategoriesPaginadoQuery> {

    @Mock
    private ITypeCategoryService typeCategoryService;

    @Mock
    private IPageableResult<TypeCategory> pageableResult;

    @Override
    protected GetAllTypeCategoriesPaginadoQuery createQuery() {
        return new GetAllTypeCategoriesPaginadoQuery(typeCategoryService);
    }

    @Override
    protected PageContext<TypeCategory> createValidContext() {
        return PageContext.<TypeCategory>builder()
                .data(TypeCategory.builder().active(true).build())
                .pageNumber(0)
                .pageSize(10)
                .sortBy("name")
                .sortDir("asc")
                .filterType("contains")
                .build();
    }

    @Override
    protected PageContext<TypeCategory> createInvalidContext() {
        return null;
    }

    @Override
    protected void assertValidResult(IPageableResult<TypeCategory> result) {
        assertSame(pageableResult, result);
    }

    @Override
    protected void mockDependencies() {
        when(typeCategoryService.getComboGrande(any(TypeCategory.class), anyInt(), anyInt(), anyString(), anyString(), anyString()))
                .thenReturn(pageableResult);
    }

    @Override
    protected PageContext<TypeCategory> createPartialContext() {
        return null;
    }

    @Override
    protected void verifyExecutionOrder() {
        // Validado por el flujo base de QueryAbstract.
    }

    @Override
    protected void verifyPostProcess(IPageableResult<TypeCategory> result) {
        assertSame(pageableResult, result);
    }

    @Test
    void shouldThrowExactErrorWhenContextIsNull() {
        DomainException ex = assertThrows(DomainException.class, () -> query.execute(null));

        assertEquals(DomainErrors.ERROR_CONTEXTO_EMPTY, ex.getMessage());
    }
}

