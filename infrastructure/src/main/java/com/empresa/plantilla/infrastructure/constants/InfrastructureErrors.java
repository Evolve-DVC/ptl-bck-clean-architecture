package com.empresa.plantilla.infrastructure.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InfrastructureErrors {
    public static final String TOKEN_REPLACE = "REPLACE_TOKEN";

    // ========================================
    // INFRAESTRUCTURA - ERRORES GENERALES
    // ========================================
    public static final String ERROR_NO_REGISTRO_BY_ID = "error.infrastructure.no.registro.by.id";
    public static final String ERROR_INVALID_PAGINATION_CONTEXT = "error.infrastructure.pagination.context.invalid";
    public static final String ERROR_INVALID_PAGINATION_DATA = "error.infrastructure.pagination.data.invalid";

    // ========================================
    // VALIDACION - TYPE
    // ========================================
    public static final String VALIDATION_TYPE_ID_REQUIRED = "validation.type.id.required";
    public static final String VALIDATION_TYPE_CATEGORY_ID_REQUIRED = "validation.type.categoryId.required";
    public static final String VALIDATION_TYPE_NAME_REQUIRED = "validation.type.name.required";
    public static final String VALIDATION_TYPE_NAME_SIZE = "validation.type.name.size";
    public static final String VALIDATION_TYPE_CODE_REQUIRED = "validation.type.code.required";
    public static final String VALIDATION_TYPE_CODE_SIZE = "validation.type.code.size";
    public static final String VALIDATION_TYPE_DESCRIPTION_SIZE = "validation.type.description.size";

    // ========================================
    // VALIDACION - TYPE CATEGORY
    // ========================================
    public static final String VALIDATION_CATEGORY_ID_REQUIRED = "validation.typeCategory.id.required";
    public static final String VALIDATION_CATEGORY_NAME_REQUIRED = "validation.typeCategory.name.required";
    public static final String VALIDATION_CATEGORY_NAME_SIZE = "validation.typeCategory.name.size";
    public static final String VALIDATION_CATEGORY_CODE_REQUIRED = "validation.typeCategory.code.required";
    public static final String VALIDATION_CATEGORY_CODE_SIZE = "validation.typeCategory.code.size";
    public static final String VALIDATION_CATEGORY_DESCRIPTION_SIZE = "validation.typeCategory.description.size";
}
