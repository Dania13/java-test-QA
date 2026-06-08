package helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Утилитарный класс для форматирования дат в ISO-8601 формат с часовым поясом UTC.
 * <p>
 * Преобразует объекты {@link Date} в строковый формат "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
 * где время фиксировано на 12:00:00 UTC.
 * </p>
 */
public class DateFormatter {

    /** Форматтер даты с шаблоном yyyy-MM-dd и часовым поясом UTC */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Преобразует дату в строку формата ISO-8601 с временем 12:00:00 UTC.
     * <p>
     * Пример: 1990-05-15T12:00:00.000Z
     * </p>
     *
     * @param date объект даты для форматирования (не должен быть null)
     * @return строка в формате "yyyy-MM-ddT12:00:00.000Z"
     */
    public static String toCustomFormat(Date date) {
        String datePart = DATE_FORMAT.format(date);
        return datePart + "T12:00:00.000Z";
    }
}