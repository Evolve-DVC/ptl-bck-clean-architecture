package com.empresa.plantilla.domain.query.type;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.domain.query.QueryTestAbstract;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GetTypesByTypeCategoryQueryTest extends QueryTestAbstract<Type, List<Type>, GetTypesByTypeCategoryQuery> {

    @Mock
    private ITypeService typeService;

    @Override
    protected GetTypesByTypeCategoryQuery createQuery() {
        return new GetTypesByTypeCategoryQuery(typeService);
    }

    @Override
    protected Type createValidContext() {
        return Type.builder().typeCategoryId(1L).build();
    }

    @Override
    protected Type createInvalidContext() {
        return null;
    }

    @Override
    protected void assertValidResult(List<Type> result) {
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
    }

    @Override
    protected void mockDependencies() {
        when(typeService.getComboSencillo(any(Type.class))).thenReturn(List.of(Type.builder().id(1L).build()));
    }

    @Override
    protected Type createPartialContext() {
        return null;
    }

    @Override
    protected void verifyExecutionOrder() {
        // El flujo es validado por QueryAbstract.execute en testExecutionOrder.
    }

    @Override
    protected void verifyPostProcess(List<Type> result) {
        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowExactErrorWhenContextIsNull() {
        DomainException ex = assertThrows(DomainException.class, () -> query.execute(null));
        assertEquals(DomainErrors.ERROR_CONTEXTO_EMPTY, ex.getMessage());
    }
}

