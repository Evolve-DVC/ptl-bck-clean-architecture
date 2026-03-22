package com.empresa.plantilla.domain.query.typeCategory;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.TypeCategory;
import com.empresa.plantilla.domain.query.QueryTestAbstract;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GetAllTypeCategoriesQueryTest extends QueryTestAbstract<TypeCategory, List<TypeCategory>, GetAllTypeCategoriesQuery> {

    @Mock
    private ITypeCategoryService typeCategoryService;

    @Override
    protected GetAllTypeCategoriesQuery createQuery() {
        return new GetAllTypeCategoriesQuery(typeCategoryService);
    }

    @Override
    protected TypeCategory createValidContext() {
        return TypeCategory.builder().active(true).build();
    }

    @Override
    protected TypeCategory createInvalidContext() {
        return null;
    }

    @Override
    protected void assertValidResult(List<TypeCategory> result) {
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
    }

    @Override
    protected void mockDependencies() {
        when(typeCategoryService.getComboSencillo(any(TypeCategory.class)))
                .thenReturn(List.of(TypeCategory.builder().id(1L).name("Cat A").build()));
    }

    @Override
    protected TypeCategory createPartialContext() {
        return null;
    }

    @Override
    protected void verifyExecutionOrder() {
        // Validado por el flujo de QueryAbstract.
    }

    @Override
    protected void verifyPostProcess(List<TypeCategory> result) {
        assertEquals("Cat A", result.getFirst().getName());
    }

    @Test
    void shouldThrowExactErrorWhenContextIsNull() {
        DomainException ex = assertThrows(DomainException.class, () -> query.execute(null));

        assertEquals(DomainErrors.ERROR_CONTEXTO_EMPTY, ex.getMessage());
    }
}

