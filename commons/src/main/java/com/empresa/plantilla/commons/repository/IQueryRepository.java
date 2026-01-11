package com.empresa.plantilla.commons.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para operaciones de consulta de repositorio.
 *
 * @param <E> el tipo de entidad que será consultada
 * @param <K> el tipo del identificador de la entidad
 */
public interface IQueryRepository<E, K> {
    /**
     * Busca todos los elementos que coinciden con el ejemplo proporcionado,
     * aplicando paginación.
     *
     * @param example el ejemplo de entidad para filtrar la búsqueda
     * @param pageable la información de paginación
     * @return una página de entidades que coinciden con el ejemplo
     */
    Page<E> findAll(Example<E> example, Pageable pageable);

    /**
     * Busca todos los elementos que coinciden con el ejemplo proporcionado.
     *
     * @param example el ejemplo de entidad para filtrar la búsqueda
     * @return una lista de entidades que coinciden con el ejemplo
     */
    List<E> findAll(Example<E> example);

    /**
     * Busca un elemento por su identificador.
     *
     * @param id el identificador del elemento
     * @return un Optional que contiene la entidad si se encuentra, o vacío si no
     */
    Optional<E> findById(K id);

    /**
     * Verifica si un elemento existe por su identificador.
     *
     * @param id el identificador del elemento
     * @return true si el elemento existe, false en caso contrario
     */
    boolean existsById(K id);

    /**
     * Obtiene el siguiente valor de una secuencia para el identificador.
     *
     * @return el siguiente valor de la secuencia
     */
    K getNextValSequence();
}
