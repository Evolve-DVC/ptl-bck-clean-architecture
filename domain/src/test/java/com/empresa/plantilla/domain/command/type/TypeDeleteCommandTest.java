package com.empresa.plantilla.domain.command.type;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.command.CommandProcessTestAbstract;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.Type;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TypeDeleteCommandTest extends CommandProcessTestAbstract<Type, Type, TypeDeleteCommand> {

    @Mock
    private ITypeService typeService;

    @Override
    protected TypeDeleteCommand createCommand() {
        return new TypeDeleteCommand(typeService, executor);
    }

    @Override
    protected Type createValidContext() {
        return Type.builder().id(1L).build();
    }

    @Override
    protected Type createInvalidContext() {
        return null;
    }

    @Override
    protected void assertValidResult(Type result) {
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Override
    protected void setMethod() {
        when(typeService.delete(any(Type.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        Type context = Type.builder().build();
        command.setContext(context);

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_CONTEXTO_EMPTY, ex.getMessage());
    }

    @Override
    @Test
    public void testPreProcess() {
        Type context = createValidContext();
        command.setContext(context);
        setMethod();

        Type result = command.execute();

        assertNotNull(result);
    }

    @Override
    @Test
    public void testPostProcess() {
        Type context = createValidContext();
        command.setContext(context);
        setMethod();

        Type result = command.execute();

        assertEquals(context, result);
    }
}

