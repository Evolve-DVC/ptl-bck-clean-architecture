package com.empresa.plantilla.domain.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Modelo de dominio que representa una categoría de tipos.
 *
 * <p>El código de la categoría debe ser único de forma global.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class TypeCategory {

    /**
     * Identificador único de la categoría.
     */
    private Long id;

    /**
     * Nombre de la categoría (requerido).
     */
    private String name;

    /**
     * Código único de la categoría a nivel global (requerido).
     */
    private String code;

    /**
     * Descripción adicional de la categoría.
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

