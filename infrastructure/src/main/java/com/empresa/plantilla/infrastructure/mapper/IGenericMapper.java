package com.empresa.plantilla.infrastructure.mapper;

import java.util.List;

/**
 * Interfaz genérica para mapeo entre diferentes capas de la aplicación.
 *
 * @param <M> El tipo del modelo de dominio
 * @param <E> El tipo de la entidad
 * @param <K> El tipo de la clave
 */
public interface IGenericMapper<M, E, K> {
    /**
     * Convierte un modelo de dominio a una entidad.
     *
     * @param model El modelo de dominio a convertir
     * @return La entidad resultante
     */
    E modelToEntity(M model);

    /**
     * Convierte una entidad a un modelo de dominio.
     *
     * @param entity La entidad a convertir
     * @return El modelo de dominio resultante
     */
    M entityToModel(E entity);

    /**
     * Convierte una colección de modelos de dominio a una lista de entidades.
     *
     * @param models La colección de modelos de dominio a convertir
     * @return La lista de entidades resultante
     */
    List<E> toEntityList(Iterable<M> models);

    /**
     * Convierte una colección de entidades a una lista de modelos de dominio.
     *
     * @param entities La colección de entidades a convertir
     * @return La lista de modelos de dominio resultante
     */
    List<M> toModelList(Iterable<E> entities);

    /**
     * Extrae la clave de un modelo de dominio.
     *
     * @param model El modelo de dominio del cual extraer la clave
     * @return La clave extraída
     */
    K toKey(M model);

}