package com.empresa.plantilla.infrastructure.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constantes de capa REST y documentacion asociada.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestConstants {

    public static final String API_BASE = "/api";

    public static final String API_TYPE_CATEGORY = API_BASE + "/type-categories";
    public static final String API_TYPE = API_BASE + "/types";

    public static final String PATH_ID = "/{id}";
    public static final String PATH_COMBO = "/combo";
    public static final String PATH_PAGINADO = "/paginado";

    public static final String TAG_TYPE_CATEGORY = "TypeCategory";
    public static final String TAG_TYPE = "Type";

    // Claves i18n de mensajes REST
    public static final String MSG_TYPE_CATEGORY_CREATED = "success.typeCategory.created";
    public static final String MSG_TYPE_CATEGORY_UPDATED = "success.typeCategory.updated";
    public static final String MSG_TYPE_CATEGORY_DELETED = "success.typeCategory.deleted";
    public static final String MSG_TYPE_CATEGORY_FOUND = "success.typeCategory.found";
    public static final String MSG_TYPE_CATEGORY_LIST = "success.typeCategory.list";
    public static final String MSG_TYPE_CATEGORY_PAGE = "success.typeCategory.page";

    public static final String MSG_TYPE_CREATED = "success.type.created";
    public static final String MSG_TYPE_UPDATED = "success.type.updated";
    public static final String MSG_TYPE_DELETED = "success.type.deleted";
    public static final String MSG_TYPE_FOUND = "success.type.found";
    public static final String MSG_TYPE_LIST = "success.type.list";
    public static final String MSG_TYPE_PAGE = "success.type.page";

    // Claves i18n para texto de Swagger
    public static final String DOC_TYPE_CATEGORY_CONTROLLER = "doc.typeCategory.controller";
    public static final String DOC_TYPE_CONTROLLER = "doc.type.controller";

    public static final String DOC_TYPE_CREATE = "doc.type.create";
    public static final String DOC_TYPE_UPDATE = "doc.type.update";
    public static final String DOC_TYPE_DELETE = "doc.type.delete";
    public static final String DOC_TYPE_GET_BY_ID = "doc.type.getById";
    public static final String DOC_TYPE_COMBO = "doc.type.combo";
    public static final String DOC_TYPE_PAGINADO = "doc.type.paginado";

    public static final String DOC_TYPE_CATEGORY_CREATE = "doc.typeCategory.create";
    public static final String DOC_TYPE_CATEGORY_UPDATE = "doc.typeCategory.update";
    public static final String DOC_TYPE_CATEGORY_DELETE = "doc.typeCategory.delete";
    public static final String DOC_TYPE_CATEGORY_GET_BY_ID = "doc.typeCategory.getById";
    public static final String DOC_TYPE_CATEGORY_COMBO = "doc.typeCategory.combo";
    public static final String DOC_TYPE_CATEGORY_PAGINADO = "doc.typeCategory.paginado";

    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIR = "asc";
    public static final String DEFAULT_FILTER_TYPE = "CONTAINING";
}
