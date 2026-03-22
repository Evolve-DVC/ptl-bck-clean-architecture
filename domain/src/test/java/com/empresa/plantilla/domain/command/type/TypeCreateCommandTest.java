package com.empresa.plantilla.domain.command.type;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.type.ITypeService;
import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.command.CommandProcessTestAbstract;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.Type;
import com.empresa.plantilla.domain.model.TypeCategory;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class TypeCreateCommandTest extends CommandProcessTestAbstract<Type, Type, TypeCreateCommand> {

    @Mock
    private ITypeService typeService;

    @Mock
    private ITypeCategoryService typeCategoryService;

    @Override
    protected TypeCreateCommand createCommand() {
        return new TypeCreateCommand(typeService, typeCategoryService, executor);
    }

    @Override
    protected Type createValidContext() {
        return Type.builder().typeCategoryId(1L).name("Type A").code("A").build();
    }

    @Override
    protected Type createInvalidContext() {
        return null;
    }

    @Override
    protected void assertValidResult(Type result) {
        assertNotNull(result);
        assertEquals("Type A", result.getName());
    }

    @Override
    protected void setMethod() {
        when(typeCategoryService.getElement(anyLong())).thenReturn(TypeCategory.builder().id(1L).build());
        when(typeService.getComboSencillo(any(Type.class))).thenReturn(List.of());
        when(typeService.save(any(Type.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldThrowWhenTypeCategoryIdIsNull() {
        Type context = Type.builder().name("Type A").code("A").build();
        command.setContext(context);

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_TYPE_CATEGORY_ID_EMPTY, ex.getMessage());
    }

    @Test
    void shouldThrowWhenTypeCategoryDoesNotExist() {
        Type context = createValidContext();
        command.setContext(context);
        when(typeCategoryService.getElement(anyLong())).thenReturn(null);

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_TYPE_CATEGORY_NOT_FOUND, ex.getMessage());
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        Type context = Type.builder().typeCategoryId(1L).name(" ").code("A").build();
        command.setContext(context);
        when(typeCategoryService.getElement(anyLong())).thenReturn(TypeCategory.builder().id(1L).build());

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_TYPE_NAME_EMPTY, ex.getMessage());
    }

    @Test
    void shouldThrowWhenCodeIsBlank() {
        Type context = Type.builder().typeCategoryId(1L).name("Type A").code(" ").build();
        command.setContext(context);
        when(typeCategoryService.getElement(anyLong())).thenReturn(TypeCategory.builder().id(1L).build());

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_TYPE_CODE_EMPTY, ex.getMessage());
    }

    @Test
    void shouldThrowWhenCodeAlreadyExistsInCategory() {
        Type context = createValidContext();
        command.setContext(context);
        when(typeCategoryService.getElement(anyLong())).thenReturn(TypeCategory.builder().id(1L).build());
        when(typeService.getComboSencillo(any(Type.class))).thenReturn(List.of(Type.builder().id(10L).build()));

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_TYPE_CODE_DUPLICATE, ex.getMessage());
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

