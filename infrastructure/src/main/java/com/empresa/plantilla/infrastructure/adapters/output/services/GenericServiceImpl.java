package com.empresa.plantilla.infrastructure.adapters.output.services;

import com.empresa.plantilla.commons.exception.InfrastructureException;
import com.empresa.plantilla.commons.repository.ICommandRepository;
import com.empresa.plantilla.commons.repository.IQueryRepository;
import com.empresa.plantilla.commons.services.IGenericService;
import com.empresa.plantilla.commons.services.pageable.IPageableResult;
import com.empresa.plantilla.infrastructure.adapters.output.services.pageable.PageableResultImpl;
import com.empresa.plantilla.infrastructure.mapper.IGenericMapper;
import org.springframework.data.domain.*;

import java.util.List;

import static com.empresa.plantilla.infrastructure.constants.InfrastructureErrors.ERROR_NO_REGISTRO_BY_ID;
import static com.empresa.plantilla.infrastructure.constants.InfrastructureErrors.TOKEN_REPLACE;

/**
 * Implementación abstracta genérica para servicios CRUD con patrón CQRS.
 *
 * @param <M> Tipo del modelo
 * @param <E> Tipo de la entidad
 * @param <K> Tipo de la clave primaria
 */
public abstract class GenericServiceImpl<M, E, K> implements IGenericService<M, K> {

    /**
     * Obtiene el repositorio JPA para operaciones de comando (escritura).
     *
     * @return Repositorio JPA para comandos
     */
    protected abstract ICommandRepository<M, K> getCommandRepository();

    /**
     * Obtiene el repositorio JPA para operaciones de consulta (lectura).
     *
     * @return Repositorio JPA para consultas
     */
    protected abstract IQueryRepository<M, K> getQueryRepository();

    /**
     * Obtiene el mapper para conversiones entre DTO, modelo y entidad.
     *
     * @return Mapper genérico
     */
    protected abstract IGenericMapper<M, E, K> getMapper();

    /**
     * Obtiene una lista paginada y ordenada de elementos con filtros.
     *
     * @param model      Objeto DTO con los criterios de filtrado
     * @param pageNumber Número de página
     * @param pageSize   Tamaño de página
     * @param sortBy     Campo por el cual ordenar
     * @param sortDir    Dirección del ordenamiento ('asc' o 'desc')
     * @param filterType Tipo de filtro ('STARTING', 'CONTAINING', 'ENDING', 'EXACT')
     * @return Resultado paginado de elementos
     */
    @Override
    public IPageableResult<M> getComboGrande(M model, int pageNumber, int pageSize, String sortBy, String sortDir, String filterType) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        ExampleMatcher matcher = switch (filterType) {
            case "STARTING" ->
                    ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.STARTING).withIgnoreCase();
            case "CONTAINING" ->
                    ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();
            case "ENDING" ->
                    ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.ENDING).withIgnoreCase();
            case "EXACT" ->
                    ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.EXACT).withIgnoreCase();
            default ->
                    ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();
        };

        Example<M> example = Example.of(model, matcher);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        // Ahora llama al repositorio del DOMINIO
        Page<M> result = getQueryRepository().findAll(example, pageRequest);
        return new PageableResultImpl<>(result);
    }

    /**
     * Obtiene una lista simple de elementos aplicando los criterios de filtrado.
     *
     * @param model Objeto Model con los criterios de filtrado
     * @return Lista de elementos filtrados
     */
    @Override
    public List<M> getComboSencillo(M model) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<M> example = Example.of(model, matcher);
        return getQueryRepository().findAll(example);
    }

    /**
     * Obtiene un elemento por su identificador.
     *
     * @param id Identificador del elemento
     * @return Elemento encontrado
     * @throws InfrastructureException si no se encuentra el elemento
     */
    @Override
    public M getElement(K id) {
        return getQueryRepository().findById(id)
                .orElseThrow(() -> new InfrastructureException(ERROR_NO_REGISTRO_BY_ID.replace(TOKEN_REPLACE, String.valueOf(id))));
    }

    /**
     * Guarda un nuevo elemento.
     *
     * @param model Elemento a guardar
     * @return Elemento guardado
     */
    @Override
    public M save(M model) {
        return getCommandRepository().save(model);
    }

    /**
     * Guarda múltiples elementos.
     *
     * @param models Elementos a guardar
     * @return Elementos guardados
     */
    @Override
    public Iterable<M> saveAll(Iterable<M> models) {
        return getCommandRepository().saveAll(models);
    }

    /**
     * Actualiza un elemento existente.
     *
     * @param model Elemento a actualizar
     * @return Elemento actualizado
     */
    @Override
    public M update(M model) {
        return getCommandRepository().update(model);
    }

    /**
     * Actualiza múltiples elementos.
     *
     * @param models Elementos a actualizar
     * @return Elementos actualizados
     */
    @Override
    public Iterable<M> updateAll(Iterable<M> models) {
        return getCommandRepository().updateAll(models);
    }

    /**
     * Elimina un elemento.
     *
     * @param model Elemento a eliminar
     * @return Elemento eliminado
     * @throws InfrastructureException si no se encuentra el elemento
     */
    @Override
    public M delete(M model) {
        K key = getMapper().toKey(model);
        M existingModel = getElement(key);
        getCommandRepository().delete(key);
        return existingModel;
    }

    /**
     * Verifica si existe un elemento por su identificador.
     *
     * @param id Identificador del elemento
     * @return true si existe, false en caso contrario
     */
    @Override
    public boolean existById(K id) {
        return getQueryRepository().existsById(id);
    }

    /**
     * Obtiene el siguiente valor de la secuencia para el identificador.
     *
     * @return Siguiente valor de la secuencia
     */
    @Override
    public K getNextId() {
        return getQueryRepository().getNextValSequence();
    }
}

