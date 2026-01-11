package com.empresa.plantilla.commons.services.token;

import com.empresa.plantilla.commons.util.security.Base64TokenGenerator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Base64TokenService {

    /**
    * Genera un token de autorización basado en la entidad de servicio y la clase de servicio proporcionadas.
    *
    * @param entidadServicio La entidad de servicio para la cual se genera el token
    * @param claseServicio El identificador de la clase de servicio
    * @return Una cadena codificada en Base64 que representa el token de autorización
    */
    public static String getBase64Token(String entidadServicio, int claseServicio) {
         Base64TokenGenerator base64TokenGenerator = new Base64TokenGenerator(entidadServicio, String.valueOf(claseServicio));
         return base64TokenGenerator.base64Hash();
    }
}