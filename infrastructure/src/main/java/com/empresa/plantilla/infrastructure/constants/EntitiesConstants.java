package com.empresa.plantilla.infrastructure.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constantes de metadatos de entidades JPA.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntitiesConstants {

    public static final String PACKAGE_ENTITIES = "com.empresa.plantilla.infrastructure.entities";

    // TypeCategory
    public static final String TABLE_TYPE_CATEGORY = "type_category";
    public static final String SEQ_TYPE_CATEGORY_ID = "seq_type_category_id";
    public static final String UQ_TYPE_CATEGORY_CODE = "uk_type_category_code";

    // Type
    public static final String TABLE_TYPE = "type";
    public static final String SEQ_TYPE_ID = "seq_type_id";
    public static final String UQ_TYPE_CATEGORY_CODE_BY_CATEGORY = "uk_type_category_code_by_category";

    // Columnas comunes
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_CODE = "code";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_ACTIVE = "active";
    public static final String COL_CREATE_BY = "create_by";
    public static final String COL_CREATE_DATE = "create_date";
    public static final String COL_UPDATE_BY = "update_by";
    public static final String COL_UPDATE_DATE = "update_date";

    // Columnas especificas
    public static final String COL_TYPE_CATEGORY_ID = "type_category_id";
}
