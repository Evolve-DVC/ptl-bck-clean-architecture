package com.empresa.plantilla.infrastructure.adapters.output.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Interfaz genérica para repositorios JPA en el proyecto.
 *
 * <p>Esta interfaz extiende {@link JpaRepository} y proporciona métodos básicos
 * para operaciones CRUD y consultas en entidades JPA.</p>
 *
 * <p>La anotación {@link NoRepositoryBean} indica que esta interfaz no debe ser
 * considerada como un repositorio en sí misma, sino que debe ser extendida por
 * otras interfaces específicas de repositorios.</p>
 *
 * @param <E> El tipo de la entidad que será manejada por el repositorio.
 * @param <K> El tipo de la clave primaria de la entidad.
 */
@NoRepositoryBean
public interface IGenericJpaRepository<E, K> extends JpaRepository<E, K> {

    /**
     * Obtiene el siguiente valor de una secuencia basada en la entidad proporcionada.
     *
     * @return El siguiente valor de la secuencia.
     */
    @Query("SELECT COALESCE(MAX(e.id), 0) + 1 FROM #{#entityName} e")
    K getNextValSequence();
}
