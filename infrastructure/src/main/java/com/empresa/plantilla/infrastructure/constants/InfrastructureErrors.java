package com.empresa.plantilla.infrastructure.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InfrastructureErrors {
    public static final String TOKEN_REPLACE = "REPLACE_TOKEN";

    public static final String ERROR_NO_REGISTRO_BY_ID = "{error.infrastructure.no.registro.by.id}";
}
