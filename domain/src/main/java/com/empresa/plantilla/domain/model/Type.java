package com.empresa.plantilla.domain.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Modelo de dominio que representa un tipo dentro de una categoría.
 *
 * <p>El código de {@code Type} debe ser único dentro de su categoría
 * ({@code typeCategoryId + code}).</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Type {

    /**
     * Identificador único del tipo.
     */
    private Long id;

    /**
     * Identificador de la categoría a la que pertenece este tipo (requerido).
     * Un tipo no puede existir sin una categoría asociada.
     */
    private Long typeCategoryId;

    /**
     * Nombre funcional del tipo (requerido).
     */
    private String name;

    /**
     * Código único del tipo dentro de su categoría (requerido).
     * Debe ser único para la combinación (typeCategoryId, code).
     */
    private String code;

    /**
     * Descripción adicional del tipo.
     */
    private String description;

    /**
     * Indicador de vigencia del registro.
     */
    private Boolean active;

    /**
     * Usuario que crea el registro.
     */
    private String createBy;

    /**
     * Fecha y hora de creación del registro.
     */
    private LocalDateTime createDate;

    /**
     * Usuario que realiza la última actualización.
     */
    private String updateBy;

    /**
     * Fecha y hora de la última actualización.
     */
    private LocalDateTime updateDate;

}

