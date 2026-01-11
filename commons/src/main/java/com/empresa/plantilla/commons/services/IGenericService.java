package com.empresa.plantilla.commons.services;

import com.empresa.plantilla.commons.services.pageable.IPageableResult;

import java.util.List;

/**
 * Interfaz genérica que define operaciones CRUD básicas y métodos de consulta.
 *
 * @param <M> tipo del modelo de dominio
 * @param <K> tipo de la clave primaria
 */
public interface IGenericService<M, K> {

    /**
     * Obtiene una lista paginada de elementos con opciones de ordenamiento y filtrado.
     *
     * @param model modelo para filtrar
     * @param pageNumber número de página
     * @param pageSize tamaño de página
     * @param sortBy campo por el cual ordenar
     * @param sortDir dirección del ordenamiento
     * @param filterType tipo de filtro a aplicar
     * @return resultado paginado de elementos
     */
    IPageableResult<M> getComboGrande(M model, int pageNumber, int pageSize, String sortBy, String sortDir, String filterType);

    /**
     * Obtiene una lista simple de elementos filtrados.
     *
     * @param model modelo para filtrar
     * @return lista de elementos
     */
    List<M> getComboSencillo(M model);

    /**
     * Obtiene un elemento por su identificador.
     *
     * @param id identificador del elemento
     * @return elemento encontrado
     */
    M getElement(K id);

    /**
     * Guarda un nuevo elemento.
     *
     * @param model elemento a guardar
     * @return elemento guardado
     */
    M save(M model);

    /**
     * Guarda múltiples elementos.
     *
     * @param models elementos a guardar
     * @return elementos guardados
     */
    Iterable<M> saveAll(Iterable<M> models);

    /**
     * Actualiza un elemento existente.
     *
     * @param model elemento a actualizar
     * @return elemento actualizado
     */
    M update(M model);

    /**
     * Actualiza múltiples elementos.
     *
     * @param models elementos a actualizar
     * @return elementos actualizados
     */
    Iterable<M> updateAll(Iterable<M> models);

    /**
     * Elimina un elemento.
     *
     * @param model elemento a eliminar
     * @return elemento eliminado
     */
    M delete(M model);

    /**
     * Verifica si existe un elemento por su identificador.
     *
     * @param id identificador a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existById(K id);

    /**
     * Obtiene el siguiente identificador disponible.
     *
     * @return siguiente identificador
     */
    K getNextId();
}