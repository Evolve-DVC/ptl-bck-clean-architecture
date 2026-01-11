package com.empresa.plantilla.commons.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constantes para las claves de mensajes internacionalizados.
 * Estas claves se utilizan para obtener mensajes desde los archivos de properties.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageKeys {

    // ========================================
    // MENSAJES DE ÉXITO
    // ========================================
    public static final String SUCCESS_OPERATION = "success.operation";
    public static final String SUCCESS_CREATED = "success.created";
    public static final String SUCCESS_NO_CONTENT = "success.no.content";
    public static final String SUCCESS_PAGINATED = "success.paginated";
    public static final String SUCCESS_NO_RESULTS = "success.no.results";
    public static final String SUCCESS_PAGE_INFO = "success.page.info";

    // ========================================
    // ERRORES GENERALES
    // ========================================
    public static final String ERROR_INTERNAL_SERVER = "error.internal.server";
    public static final String ERROR_BAD_REQUEST = "error.bad.request";
    public static final String ERROR_NOT_FOUND = "error.not.found";
    public static final String ERROR_UNAUTHORIZED = "error.unauthorized";
    public static final String ERROR_FORBIDDEN = "error.forbidden";
    public static final String ERROR_NULL_POINTER = "error.null.pointer";

    // ========================================
    // ERRORES DE VALIDACIÓN
    // ========================================
    public static final String ERROR_VALIDATION_PREFIX = "error.validation.prefix";
    public static final String ERROR_CONSTRAINT_VIOLATION = "error.constraint.violation";
    public static final String ERROR_ILLEGAL_ARGUMENT = "error.illegal.argument";
    public static final String ERROR_TYPE_MISMATCH = "error.type.mismatch";
    public static final String ERROR_JSON_INVALID = "error.json.invalid";
    public static final String ERROR_METHOD_NOT_SUPPORTED = "error.method.not.supported";
    public static final String ERROR_MEDIA_TYPE_NOT_SUPPORTED = "error.media.type.not.supported";
    public static final String ERROR_PARAMETER_MISSING = "error.parameter.missing";
    public static final String ERROR_ENDPOINT_NOT_FOUND = "error.endpoint.not.found";

    // ========================================
    // ERRORES DE BASE DE DATOS
    // ========================================
    public static final String ERROR_DATA_INTEGRITY = "error.data.integrity";
    public static final String ERROR_FK_CONSTRAINT = "error.fk.constraint";

    // ========================================
    // ERRORES DE DOMINIO (existentes)
    // ========================================
    public static final String ERROR_DOMAIN_VALID_ENUM = "error.domain.valid.enum";
    public static final String ERROR_DOMAIN_VALID_ID_EMPTY = "error.domain.valid.id.empty";
    public static final String ERROR_DOMAIN_VALID_CONTEXTO_NULL = "error.domain.valid.contexto.null";
    public static final String ERROR_DOMAIN_VALID_CREATE_EMPTY = "error.domain.valid.create.empty";
    public static final String ERROR_DOMAIN_VALID_UPDATE_EMPTY = "error.domain.valid.update.empty";

    // ========================================
    // ERRORES DE INFRAESTRUCTURA (existentes)
    // ========================================
    public static final String ERROR_INFRASTRUCTURE_NO_REGISTRO_BY_ID = "error.infrastructure.no.registro.by.id";
}

