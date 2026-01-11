package com.empresa.plantilla.commons.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Clase abstracta base para implementar el patrón Query.
 * Define una estructura para ejecutar consultas con pre-procesamiento, procesamiento principal y post-procesamiento.
 *
 * @param <C> El tipo de contexto que contiene los parámetros de entrada para la consulta
 * @param <R> El tipo de resultado que devolverá la consulta
 */
@Slf4j
@Component
public abstract class QueryAbstract<C, R> {

    /**
     * Ejecuta el proceso de consulta llamando a los métodos preProcess, process y postProcess en orden.
     * Este metodo es final para asegurar que el flujo de ejecución no sea alterado por las implementaciones.
     *
     * @param context El objeto de contexto que contiene la información necesaria para la ejecución de la consulta
     * @return El resultado de la consulta después del procesamiento completo
     */
    public final R execute(C context) {
        preProcess(context);
        R result = process(context);
        return postProcess(context, result);
    }

    /**
     * Realiza los pasos de pre-procesamiento necesarios antes de la ejecución principal de la consulta.
     * Las implementaciones deben definir las validaciones y preparaciones específicas del contexto.
     *
     * @param context El objeto de contexto que contiene la información necesaria para el pre-procesamiento
     */
    protected abstract void preProcess(C context);

    /**
     * Ejecuta el proceso principal de la consulta.
     * Las implementaciones deben definir la lógica específica de la consulta.
     *
     * @param context El objeto de contexto que contiene la información necesaria para la ejecución de la consulta
     * @return El resultado de la ejecución de la consulta
     */
    protected abstract R process(C context);

    /**
     * Realiza los pasos de post-procesamiento después de la ejecución principal de la consulta.
     * Este metodo puede ser sobrescrito para modificar o enriquecer el resultado antes de devolverlo.
     *
     * @param context El objeto de contexto que contiene la información necesaria para el post-procesamiento
     * @param result El resultado obtenido del proceso principal de la consulta
     * @return El resultado post-procesado, por defecto devuelve el mismo resultado sin modificaciones
     */
    protected R postProcess(C context, R result) {
        return result;
    }
}