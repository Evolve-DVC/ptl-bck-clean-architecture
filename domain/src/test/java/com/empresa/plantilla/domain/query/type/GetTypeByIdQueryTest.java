package com.empresa.plantilla.domain.query.type;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.domain.query.QueryTestAbstract;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class GetTypeByIdQueryTest extends QueryTestAbstract<Long, Type, GetTypeByIdQuery> {

    @Mock
    private ITypeService typeService;

    @Override
    protected GetTypeByIdQuery createQuery() {
        return new GetTypeByIdQuery(typeService);
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
    protected void assertValidResult(Type result) {
        assertEquals(1L, result.getId());
    }

    @Override
    protected void mockDependencies() {
        when(typeService.getElement(anyLong())).thenReturn(Type.builder().id(1L).typeCategoryId(1L).name("Type A").code("A").build());
    }

    @Override
    protected Long createPartialContext() {
        return null;
    }

    @Override
    protected void verifyExecutionOrder() {
        // El flujo execute -> preProcess -> process es cubierto por la abstracta.
    }

    @Override
    protected void verifyPostProcess(Type result) {
        assertEquals("Type A", result.getName());
    }

    @Test
    void shouldThrowExactErrorWhenIdIsNull() {
        DomainException ex = org.junit.jupiter.api.Assertions.assertThrows(DomainException.class, () -> query.execute(null));
        assertEquals(DomainErrors.ERROR_CONTEXTO_EMPTY, ex.getMessage());
    }
}

