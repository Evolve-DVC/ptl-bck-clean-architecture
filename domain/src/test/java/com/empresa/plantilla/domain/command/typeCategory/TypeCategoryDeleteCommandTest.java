package com.empresa.plantilla.domain.command.typeCategory;

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
import static org.mockito.Mockito.when;

class TypeCategoryDeleteCommandTest extends CommandProcessTestAbstract<TypeCategory, TypeCategory, TypeCategoryDeleteCommand> {

    @Mock
    private ITypeCategoryService typeCategoryService;

    @Mock
    private ITypeService typeService;

    @Override
    protected TypeCategoryDeleteCommand createCommand() {
        return new TypeCategoryDeleteCommand(typeCategoryService, typeService, executor);
    }

    @Override
    protected TypeCategory createValidContext() {
        return TypeCategory.builder().id(1L).build();
    }

    @Override
    protected TypeCategory createInvalidContext() {
        return null;
    }

    @Override
    protected void assertValidResult(TypeCategory result) {
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Override
    protected void setMethod() {
        when(typeService.getComboSencillo(any(Type.class))).thenReturn(List.of());
        when(typeCategoryService.delete(any(TypeCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        TypeCategory context = TypeCategory.builder().build();
        command.setContext(context);

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_CONTEXTO_EMPTY, ex.getMessage());
    }

    @Test
    void shouldThrowWhenCategoryHasAssociatedTypes() {
        TypeCategory context = createValidContext();
        command.setContext(context);
        when(typeService.getComboSencillo(any(Type.class))).thenReturn(List.of(Type.builder().id(10L).build()));

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_TYPE_CATEGORY_HAS_TYPES, ex.getMessage());
    }

    @Override
    @Test
    public void testPreProcess() {
        TypeCategory context = createValidContext();
        command.setContext(context);
        setMethod();

        TypeCategory result = command.execute();

        assertNotNull(result);
    }

    @Override
    @Test
    public void testPostProcess() {
        TypeCategory context = createValidContext();
        command.setContext(context);
        setMethod();

        TypeCategory result = command.execute();

        assertEquals(context, result);
    }
}

