package com.empresa.plantilla.commons.util;

import com.empresa.plantilla.commons.exception.InfrastructureException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
* Clase de utilidad para manejar atributos de objetos.
*/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectUtilities {

    /**
    * Recupera el valor de un atributo especificado de un objeto.
    *
    * @param object El objeto del cual se recuperará el atributo.
    * @param name   El nombre del atributo a recuperar.
    * @return El valor del atributo especificado.
    * @throws InfrastructureException Si ocurre un problema al recuperar el atributo.
    */
    public static Object getAttribute(Object object, String name) {
     try {
         return BeanUtils.getProperty(object, name);
     } catch (Exception e) {
         throw new InfrastructureException("Problemas al obtener el atributo: " + name);
     }
    }

    /**
    * Recupera un atributo de tipo lista de un objeto.
    *
    * @param object El objeto del cual se recuperará el atributo de tipo lista.
    * @param name   El nombre del atributo de tipo lista a recuperar.
    * @return Una lista que contiene los elementos del atributo.
    * @throws InfrastructureException Si el atributo no es una lista o un arreglo, o si ocurre un problema al recuperarlo.
    */
    public static List<Object> getListAttribute(Object object, String name) {
     try {
         Object value = PropertyUtils.getProperty(object, name);

         if (value != null && value.getClass().isArray()) {
             List<Object> list = new ArrayList<>();
             int length = Array.getLength(value);

             for (int i = 0; i < length; i++) {
                 Object element = Array.get(value, i);
                 list.add(element);
             }

             return list;
         } else if (value instanceof List) {
             return new ArrayList<>((List<?>) value);
         } else {
             throw new InfrastructureException("La propiedad " + name + " no es una lista");
         }
     } catch (Exception e) {
         throw new InfrastructureException("Problemas al obtener la lista: " + name, e);
     }
    }

    /**
    * Recupera un atributo de tipo cadena de un objeto.
    *
    * @param object El objeto del cual se recuperará el atributo de tipo cadena.
    * @param name   El nombre del atributo de tipo cadena a recuperar.
    * @return El valor del atributo como cadena, o una cadena vacía si la recuperación falla.
    */
    public static String getStringAttribute(Object object, String name) {
     try {
         return BeanUtils.getProperty(object, name);
     } catch (Exception e) {
         return "";
     }
    }

    /**
    * Recupera un atributo de tipo entero de un objeto.
    *
    * @param object El objeto del cual se recuperará el atributo de tipo entero.
    * @param name   El nombre del atributo de tipo entero a recuperar.
    * @return El valor del atributo como entero, o null si la recuperación o el análisis fallan.
    */
    public static Integer getIntegerAttribute(Object object, String name) {
     try {
         String value = BeanUtils.getProperty(object, name);
         if (value != null) {
             return Integer.parseInt(value);
         }
     } catch (Exception e) {
         return null;
     }
     return null;
    }

    /**
    * Recupera un atributo de tipo largo de un objeto.
    *
    * @param object El objeto del cual se recuperará el atributo de tipo largo.
    * @param name   El nombre del atributo de tipo largo a recuperar.
    * @return El valor del atributo como largo, o null si la recuperación o el análisis fallan.
    */
    public static Long getLongAttribute(Object object, String name) {
     try {
         String value = BeanUtils.getProperty(object, name);
         if (value != null) {
             return Long.parseLong(value);
         }
     } catch (Exception e) {
         return null;
     }
     return null;
    }

    /**
    * Recupera un atributo de tipo booleano de un objeto.
    *
    * @param object El objeto del cual se recuperará el atributo de tipo booleano.
    * @param name   El nombre del atributo de tipo booleano a recuperar.
    * @return El valor del atributo como booleano, o false si la recuperación o el análisis fallan.
    */
    public static Boolean getBooleanAttribute(Object object, String name) {
     try {
         String value = BeanUtils.getProperty(object, name);
         if (value != null) {
             return Boolean.parseBoolean(value);
         }
     } catch (Exception e) {
         return Boolean.FALSE;
     }
     return Boolean.FALSE;
    }
}