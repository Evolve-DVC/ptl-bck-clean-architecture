package com.empresa.plantilla.domain.adapters.output.repository.command.type;

import com.empresa.plantilla.commons.repository.ICommandRepository;
import com.empresa.plantilla.domain.model.Type;

/**
 * Puerto de salida para operaciones command de {@link Type}.
 */
public interface ITypeCommandRepository extends ICommandRepository<Type, Long> {
}
