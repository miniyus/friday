package com.meteormin.friday.common.util;

import org.openapitools.jackson.nullable.JsonNullable;

public class JsonNullableUtil {

    private JsonNullableUtil() {
    }

    public static <T> T unwrap(JsonNullable<T> jsonNullable, T orElse) {
        return jsonNullable == null ? null : jsonNullable.orElse(orElse);
    }

    public static <T> boolean isPresent(JsonNullable<T> jsonNullable) {
        return jsonNullable != null && jsonNullable.isPresent();
    }
}
