package com.empresa.plantilla.commons.util.security;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
* Clase que representa la generación y validación de tokens.
* Proporciona métodos para generar un hash codificado en Base64 y validar tokens.
*/
@Slf4j
@Getter
@Setter
public class Base64TokenGenerator {
    private String entidad; // Entidad asociada al token
    private String codigoServicio; // Código de servicio asociado al token
    private int hashCodeResult; // Resultado del cálculo del hash

    /**
    * Constructor que inicializa la entidad y el código de servicio.
    *
    * @param entidad        La entidad asociada con el token.
    * @param codigoServicio El código de servicio asociado con el token.
    */
    public Base64TokenGenerator(String entidad, String codigoServicio) {
         this.entidad = entidad;
         this.codigoServicio = codigoServicio;
    }

    /**
    * Genera un código hash para el objeto GenerarToken.
    * Este metodo sobrescribe el metodo hashCode predeterminado.
    *
    * @return Un entero que representa el código hash del objeto.
    */
    @Override
    public int hashCode() {
         final int prime = Integer.parseInt(getEntidad());
         hashCodeResult = 1;
         setCodigoServicio("11");
         hashCodeResult = prime * hashCodeResult
                 + ((getCodigoServicio() == null) ? 0
                 : getCodigoServicio().hashCode());
         hashCodeResult = prime * hashCodeResult
                 + ((getEntidad() == null) ? 0 : getEntidad().hashCode());
         return hashCodeResult;
    }

    /**
    * Compara este objeto GenerarToken con otro objeto para determinar si son iguales.
    * Este metodo sobrescribe el metodo equals predeterminado.
    *
    * @param obj El objeto a comparar con este GenerarToken.
    * @return true si los objetos son iguales, false en caso contrario.
    */
    @Override
    public boolean equals(Object obj) {
         if (this == obj)
             return true;
         if (obj == null)
             return false;
         if (getClass() != obj.getClass())
             return false;
         Base64TokenGenerator other = (Base64TokenGenerator) obj;
         if (getCodigoServicio() == null) {
             if (other.getCodigoServicio() != null)
                 return false;
         } else if (!getCodigoServicio().equals(other.getCodigoServicio()))
             return false;
         if (getEntidad() == null) {
             return other.getEntidad() == null;
         } else return getEntidad().equals(other.getEntidad());
    }

    /**
    * Genera un hash codificado en Base64 del código hash del objeto.
    *
    * @return Una cadena que contiene el hash codificado en Base64, o null si ocurre un error.
    */
    public String base64Hash() {
         Cripto cri = new Cripto();
         String b64 = null;
         try {
             b64 = cri.encodeBase64(String.valueOf(hashCode()));
         } catch (Exception e) {
             log.error("Error al validar el token", e);
         }
         return b64;
    }

    /**
    * Valida un token dado comparándolo con el hash codificado en Base64 de este objeto.
    *
    * @param tokenValidar El token a validar.
    * @return true si el token es válido, false en caso contrario.
    */
    public boolean validarToken(String tokenValidar) {
         try {
             if (base64Hash().equals(tokenValidar))
                 return true;
         } catch (Exception e) {
             log.error("Error al validar el token", e);
         }
         return false;
    }
}