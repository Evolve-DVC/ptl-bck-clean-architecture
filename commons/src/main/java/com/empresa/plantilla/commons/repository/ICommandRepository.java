package com.empresa.plantilla.commons.repository;

/**
 * Interfaz genérica para repositorios que realizan operaciones de comando (escritura).
 *
 * @param <E> tipo de la entidad que maneja el repositorio
 * @param <K> tipo del identificador de la entidad
 */
public interface ICommandRepository<E, K> {
    /**
     * Guarda una nueva entidad en el repositorio.
     *
     * @param entity la entidad a guardar
     * @return la entidad guardada, posiblemente con campos generados automáticamente
     */
    E save(E entity);

    /**
     * Guarda múltiples entidades en el repositorio.
     *
     * @param entities colección de entidades a guardar
     * @return las entidades guardadas
     */
    Iterable<E> saveAll(Iterable<E> entities);

    /**
     * Actualiza una entidad existente en el repositorio.
     *
     * @param entity la entidad a actualizar
     * @return la entidad actualizada
     */
    E update(E entity);

    /**
     * Actualiza múltiples entidades existentes en el repositorio.
     *
     * @param entities colección de entidades a actualizar
     * @return las entidades actualizadas
     */
    Iterable<E> updateAll(Iterable<E> entities);

    /**
     * Elimina una entidad del repositorio por su identificador.
     *
     * @param id el identificador de la entidad a eliminar
     */
    void delete(K id);

    /**
     * Elimina múltiples entidades del repositorio por sus identificadores.
     *
     * @param ids colección de identificadores de las entidades a eliminar
     */
    void deleteAll(Iterable<K> ids);
}
