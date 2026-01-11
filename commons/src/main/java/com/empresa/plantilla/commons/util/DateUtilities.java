package com.empresa.plantilla.commons.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
* Clase de utilidad para manejar operaciones relacionadas con fechas.
* Proporciona métodos para convertir, formatear y calcular diferencias entre fechas.
*/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtilities {
    private static final String FORMAT_DD_MM_YYYY = "dd/MM/yyyy";

    /**
    * Convierte una representación de cadena de una fecha en un objeto Date.
    * Este metodo analiza una cadena de fecha en el formato "dd/MM/yyyy" y la convierte
    * en un objeto java.util. Date. Si la cadena de entrada es null, el metodo devuelve null.
    *
    * @param date Una cadena que representa la fecha en el formato "dd/MM/yyyy".
    * @return Un objeto Date que representa la fecha analizada, o null si la entrada es null.
    */
    public static Date formatStringToDate(String date) {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY);
         if (date != null) {
             LocalDate localDate = LocalDate.parse(date, formatter);
             return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
         } else {
             return null;
         }
    }

    /**
    * Formatea una cadena de fecha desde el formato ISO 8601 a un formato de fecha personalizado.
    * Intenta analizar la cadena de entrada primero como un datetime completo en formato ISO 8601,
    * y si falla, intenta analizarla como una cadena de fecha simple.
    *
    * @param inputDate La cadena de fecha de entrada a formatear. Puede estar en formato ISO 8601
    *                  (yyyy-MM-dd'T'HH:mm:ssXXX) o en formato de fecha simple (yyyy-MM-dd).
    * @return Una cadena de fecha formateada en el formato "dd/MM/yyyy" si el análisis es exitoso,
    *         o null si la entrada es null o no puede ser analizada.
    */
    public static String formatDateString(String inputDate) {
         if (inputDate == null) {
             return null;
         }
         DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
         DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY);
         try {
             LocalDateTime dateTime = LocalDateTime.parse(inputDate, inputFormatter);
             return dateTime.format(outputFormatter);
         } catch (Exception e) {
             try {
                 LocalDate date = LocalDate.parse(inputDate.substring(0, 10));
                 return date.format(outputFormatter);
             } catch (Exception ex) {
                 return null;
             }
         }
    }

    /**
    * Calcula el número de días entre dos fechas dadas.
    * Toma dos cadenas de fecha como entrada en cualquier formato soportado,
    * las analiza en objetos LocalDate y luego calcula el número absoluto
    * de días entre estas fechas.
    *
    * @param fechaInicial Una cadena que representa la fecha inicial en cualquier formato soportado.
    * @param fechaFinal   Una cadena que representa la fecha final en cualquier formato soportado.
    * @return             Un entero que representa el número absoluto de días entre las dos fechas.
    *                     El resultado siempre es positivo, independientemente de cuál fecha sea anterior.
    *                     Devuelve -1 si alguna de las cadenas de fecha no puede ser analizada.
    */
    public static int numberOfDaysTwoDates(String fechaInicial, String fechaFinal) {
         LocalDate startDate = parseAnyDateFormat(fechaInicial);
         LocalDate endDate = parseAnyDateFormat(fechaFinal);
         if (startDate == null || endDate == null) {
             return -1; // Indica error en el análisis
         }
         return Integer.parseInt(String.valueOf(Math.abs(ChronoUnit.DAYS.between(startDate, endDate))));
    }

    /**
    * Intenta analizar una cadena de fecha en varios formatos comunes.
    * Este metodo prueba múltiples formateadores de fecha para analizar la cadena de entrada en un objeto LocalDate.
    * Soporta formatos como "dd/MM/yyyy", "yyyy-MM-dd", "MM/dd/yyyy", ISO local date y ISO offset date.
    *
    * @param dateString La cadena de fecha a analizar. Puede estar en varios formatos.
    * @return Un objeto LocalDate que representa la fecha analizada si es exitoso, o null si la cadena de fecha
    *         no pudo ser analizada por ninguno de los formateadores disponibles.
    */
    private static LocalDate parseAnyDateFormat(String dateString) {
         DateTimeFormatter[] formatters = {
             DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY),
             DateTimeFormatter.ofPattern("yyyy-MM-dd"),
             DateTimeFormatter.ofPattern("MM/dd/yyyy"),
             DateTimeFormatter.ISO_LOCAL_DATE,
             DateTimeFormatter.ISO_OFFSET_DATE
         };
         for (DateTimeFormatter formatter : formatters) {
             try {
                 return LocalDate.parse(dateString, formatter);
             } catch (Exception e) {
                 // Intenta con el siguiente formateador
             }
         }
         return null; // Si ningún formateador pudo analizar la fecha
    }

    /**
    * Convierte un objeto Date en una representación de cadena formateada.
    * Este metodo toma un objeto Date y lo formatea en una cadena
    * utilizando el patrón "dd/MM/yyyy".
    *
    * @param date El objeto Date a formatear. Puede ser null.
    * @return Una representación de cadena de la fecha en el formato "dd/MM/yyyy",
    *         o null si la fecha de entrada es null.
    */
    public static String formatDateToString(Date date) {
         if (date == null) {
             return null;
         }
         SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DD_MM_YYYY);
         return formatter.format(date);
    }
}