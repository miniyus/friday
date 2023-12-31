package com.meteormin.friday.common.util;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnumUtil {
    @Schema(name = "Select", description = "For SelectBox")
    public record Select(
        @Schema(name = "text", description = "화면에 표시될 text") String text,
        @Schema(name = "value", description = "사용 되는 실제 값") Enum<?> value
    ) {
    }

    public static <T extends Enum<?>> List<Select> toSelectList(Class<T> enumClass) {
        Enum<?>[] values = enumClass.getEnumConstants();
        List<Select> selectList = new ArrayList<>();

        if (enumClass.equals(HasOrder.class)) {
            Arrays.sort(values);
        }

        for (Enum<?> value : values) {
            var text = EnumMessageUtil.getMessage(value);
            selectList.add(new Select(text, value));
        }
        return selectList;
    }

    /**
     * A method that returns an enum constant of the specified enum type with the specified value.
     *
     * @param enumClass the Class object of the enum type from which to return a constant
     * @param value     the string value of the constant to return
     * @param <T>       the enum type whose constant is to be returned
     * @return the enum constant of the specified enum type with the specified value
     * @throws IllegalArgumentException if the specified enum type has no constant with the
     *                                  specified value
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T of(Class<T> enumClass, String value) {
        if (enumClass.isEnum()) {
            Enum<?>[] enumValues = enumClass.getEnumConstants();
            if (enumValues != null) {
                for (Enum<?> enumValue : enumValues) {
                    if (EnumUtil.validEnumValue(enumValue, value, false)) {
                        return (T) enumValue;
                    }
                }
            }
        }

        throw new IllegalArgumentException(String.format("Unknown %s: %s", enumClass, value));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T of(Class<T> enumClass, String value, boolean ignoreCase) {
        if (enumClass.isEnum()) {
            Enum<?>[] enumValues = enumClass.getEnumConstants();
            if (enumValues != null) {
                for (Enum<?> enumValue : enumValues) {
                    if (EnumUtil.validEnumValue(enumValue, value, ignoreCase)) {
                        return (T) enumValue;
                    }
                }
            }
        }

        throw new IllegalArgumentException(String.format("Unknown %s: %s", enumClass, value));
    }

    public static boolean validEnumValue(Enum<?> enumValue, String value, boolean ignoreCase) {
        if (value.equals(enumValue.name())) {
            return true;
        } else if (ignoreCase) {
            return value.equalsIgnoreCase(enumValue.name());
        } else {
            return false;
        }
    }
}
