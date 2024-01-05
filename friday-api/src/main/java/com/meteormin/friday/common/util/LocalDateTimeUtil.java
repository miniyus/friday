package com.meteormin.friday.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * LocalDateTimeUtil provides utility methods for working with LocalDateTime objects.
 *
 * @author seongminyoo
 * @since 2023/11/22
 */
public class LocalDateTimeUtil {

    /**
     * Parses a string representation of a date and time using the specified pattern.
     *
     * @param pattern     the pattern describing the date and time format
     * @param dateTimeStr the string to be parsed
     * @return the parsed LocalDateTime object
     * @throws DateTimeParseException if the string cannot be parsed as a valid date and time
     */
    public static LocalDateTime parseFromString(String pattern, String dateTimeStr)
        throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    public record LocalDateTimeRange(
        LocalDateTime startAt,
        LocalDateTime endAt
    ) {
    }

    public static LocalDateTimeRange parseRangeFromString(String pattern, String startAt,
        String endAt) {
        var start = LocalDateTimeUtil
            .parseFromString(pattern + " HH:mm:ss", startAt + " 00:00:00");
        var end = LocalDateTimeUtil
            .parseFromString(pattern + " HH:mm:ss", endAt + " 23:59:59");

        return new LocalDateTimeRange(start, end);
    }
}
