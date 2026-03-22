package com.empresa.plantilla.domain.command.typeCategory;

import com.empresa.plantilla.commons.exception.DomainException;
import com.empresa.plantilla.domain.adapters.output.services.typeCategory.ITypeCategoryService;
import com.empresa.plantilla.domain.command.CommandProcessTestAbstract;
import com.empresa.plantilla.domain.constants.DomainErrors;
import com.empresa.plantilla.domain.model.TypeCategory;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TypeCategoryCreateCommandTest extends CommandProcessTestAbstract<TypeCategory, TypeCategory, TypeCategoryCreateCommand> {

    @Mock
    private ITypeCategoryService typeCategoryService;

    @Override
    protected TypeCategoryCreateCommand createCommand() {
        return new TypeCategoryCreateCommand(typeCategoryService, executor);
    }

    @Override
    protected TypeCategory createValidContext() {
        return TypeCategory.builder().name("Cat A").code("CAT-A").build();
    }

    @Override
    protected TypeCategory createInvalidContext() {
        return null;
    }

    @Override
    protected void assertValidResult(TypeCategory result) {
        assertNotNull(result);
        assertEquals("Cat A", result.getName());
    }

    @Override
    protected void setMethod() {
        when(typeCategoryService.getComboSencillo(any(TypeCategory.class))).thenReturn(List.of());
        when(typeCategoryService.save(any(TypeCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        TypeCategory context = TypeCategory.builder().name(" ").code("CAT-A").build();
        command.setContext(context);

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_TYPE_CATEGORY_NAME_EMPTY, ex.getMessage());
    }

    @Test
    void shouldThrowWhenCodeIsBlank() {
        TypeCategory context = TypeCategory.builder().name("Cat A").code(" ").build();
        command.setContext(context);

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_TYPE_CATEGORY_CODE_EMPTY, ex.getMessage());
    }

    @Test
    void shouldThrowWhenCodeAlreadyExists() {
        TypeCategory context = createValidContext();
        command.setContext(context);
        when(typeCategoryService.getComboSencillo(any(TypeCategory.class))).thenReturn(List.of(TypeCategory.builder().id(99L).build()));

        DomainException ex = assertThrows(DomainException.class, () -> command.execute());

        assertEquals(DomainErrors.ERROR_TYPE_CATEGORY_CODE_DUPLICATE, ex.getMessage());
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

