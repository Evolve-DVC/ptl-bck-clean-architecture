package com.empresa.plantilla.domain.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Catálogo de claves de error de dominio para validaciones y reglas de negocio.
 *
 * <p>Las claves aquí definidas se resuelven en la capa de internacionalización
 * para construir los mensajes finales presentados al cliente.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DomainErrors {
    /**
     * Error genérico cuando el contexto recibido es nulo.
     */
    public static final String ERROR_CONTEXTO_EMPTY = "error.domain.valid.contexto.null";

    /**
     * Error cuando la categoría de tipo es nula.
     */
    public static final String ERROR_TYPE_CATEGORY_EMPTY = "error.domain.valid.typeCategory.empty";
    /**
     * Error cuando la categoría de tipo no existe.
     */
    public static final String ERROR_TYPE_CATEGORY_NOT_FOUND = "error.domain.valid.typeCategory.notfound";
    /**
     * Error cuando el nombre de la categoría es vacío o nulo.
     */
    public static final String ERROR_TYPE_CATEGORY_NAME_EMPTY = "error.domain.valid.typeCategory.name.empty";
    /**
     * Error cuando el código de la categoría es vacío o nulo.
     */
    public static final String ERROR_TYPE_CATEGORY_CODE_EMPTY = "error.domain.valid.typeCategory.code.empty";
    /**
     * Error cuando el código de la categoría ya existe.
     */
    public static final String ERROR_TYPE_CATEGORY_CODE_DUPLICATE = "error.domain.valid.typeCategory.code.duplicate";
    /**
     * Error cuando se intenta eliminar una categoría con tipos asociados.
     */
    public static final String ERROR_TYPE_CATEGORY_HAS_TYPES = "error.domain.valid.typeCategory.has.types";

    /**
     * Error cuando el tipo es nulo.
     */
    public static final String ERROR_TYPE_EMPTY = "error.domain.valid.type.empty";
    /**
     * Error cuando la categoría del tipo no se informa.
     */
    public static final String ERROR_TYPE_CATEGORY_ID_EMPTY = "error.domain.valid.type.categoryId.empty";
    /**
     * Error cuando el nombre del tipo es vacío o nulo.
     */
    public static final String ERROR_TYPE_NAME_EMPTY = "error.domain.valid.type.name.empty";
    /**
     * Error cuando el código del tipo es vacío o nulo.
     */
    public static final String ERROR_TYPE_CODE_EMPTY = "error.domain.valid.type.code.empty";
    /**
     * Error cuando el código del tipo está duplicado para la categoría.
     */
    public static final String ERROR_TYPE_CODE_DUPLICATE = "error.domain.valid.type.code.duplicate";
}
