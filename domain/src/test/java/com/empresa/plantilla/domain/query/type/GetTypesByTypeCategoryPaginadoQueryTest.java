package com.empresa.plantilla.domain.query.type;

import com.empresa.plantilla.commons.dto.pageable.PageContext;
import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.commons.services.pageable.IPageableResult;
import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.domain.query.QueryTestAbstract;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class GetTypesByTypeCategoryPaginadoQueryTest extends QueryTestAbstract<PageContext<Type>, IPageableResult<Type>, GetTypesByTypeCategoryPaginadoQuery> {

    @Mock
    private ITypeService typeService;

    @Mock
    private IPageableResult<Type> pageableResult;

    @Override
    protected GetTypesByTypeCategoryPaginadoQuery createQuery() {
        return new GetTypesByTypeCategoryPaginadoQuery(typeService);
    }

    @Override
    protected PageContext<Type> createValidContext() {
        return PageContext.<Type>builder()
                .data(Type.builder().typeCategoryId(1L).build())
                .pageNumber(0)
                .pageSize(10)
                .sortBy("name")
                .sortDir("asc")
                .filterType("contains")
                .build();
    }

    @Override
    protected PageContext<Type> createInvalidContext() {
        return PageContext.<Type>builder()
                .data(null)
                .pageNumber(0)
                .pageSize(10)
                .sortBy("name")
                .sortDir("asc")
                .filterType("contains")
                .build();
    }

    @Override
    protected void assertValidResult(IPageableResult<Type> result) {
        assertSame(pageableResult, result);
    }

    @Override
    protected void mockDependencies() {
        when(typeService.getComboGrande(any(Type.class), anyInt(), anyInt(), anyString(), anyString(), anyString()))
                .thenReturn(pageableResult);
    }

    @Override
    protected PageContext<Type> createPartialContext() {
        return PageContext.<Type>builder().data(Type.builder().build()).build();
    }

    @Override
    protected void verifyExecutionOrder() {
        // El flujo estandar de QueryAbstract queda cubierto en el test abstracto.
    }

    @Override
    protected void verifyPostProcess(IPageableResult<Type> result) {
        assertSame(pageableResult, result);
    }

    @Test
    void shouldThrowExactErrorWhenTypeCategoryIdIsNull() {
        PageContext<Type> context = PageContext.<Type>builder().data(Type.builder().build()).pageNumber(0).pageSize(10).build();

        DomainException ex = assertThrows(DomainException.class, () -> query.execute(context));

        assertEquals(DomainErrors.ERROR_TYPE_CATEGORY_ID_EMPTY, ex.getMessage());
    }

    @Test
    void shouldThrowExactErrorWhenContextIsNull() {
        DomainException ex = assertThrows(DomainException.class, () -> query.execute(null));

        assertEquals(DomainErrors.ERROR_CONTEXTO_EMPTY, ex.getMessage());
    }
}

