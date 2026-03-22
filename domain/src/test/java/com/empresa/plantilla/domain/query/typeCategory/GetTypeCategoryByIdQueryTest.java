package com.empresa.plantilla.domain.query.typeCategory;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.domain.query.QueryTestAbstract;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class GetTypeCategoryByIdQueryTest extends QueryTestAbstract<Long, TypeCategory, GetTypeCategoryByIdQuery> {

    @Mock
    private ITypeCategoryService typeCategoryService;

    @Override
    protected GetTypeCategoryByIdQuery createQuery() {
        return new GetTypeCategoryByIdQuery(typeCategoryService);
    }

    @Override
    protected Long createValidContext() {
        return 1L;
    }

    @Override
    protected Long createInvalidContext() {
        return null;
    }

    @Override
    protected void assertValidResult(TypeCategory result) {
        assertEquals(1L, result.getId());
        assertEquals("CAT-A", result.getCode());
    }

    @Override
    protected void mockDependencies() {
        when(typeCategoryService.getElement(anyLong()))
                .thenReturn(TypeCategory.builder().id(1L).name("Cat A").code("CAT-A").build());
    }

    @Override
    protected Long createPartialContext() {
        return null;
    }

    @Override
    protected void verifyExecutionOrder() {
        // Validado por la clase abstracta.
    }

    @Override
    protected void verifyPostProcess(TypeCategory result) {
        assertEquals("Cat A", result.getName());
    }

    @Test
    void shouldThrowExactErrorWhenIdIsNull() {
        DomainException ex = assertThrows(DomainException.class, () -> query.execute(null));

        assertEquals(DomainErrors.ERROR_CONTEXTO_EMPTY, ex.getMessage());
    }
}

