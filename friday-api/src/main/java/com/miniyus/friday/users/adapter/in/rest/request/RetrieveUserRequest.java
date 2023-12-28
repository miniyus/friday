package com.miniyus.friday.users.adapter.in.rest.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miniyus.friday.common.util.LocalDateTimeUtil;
import com.miniyus.friday.users.domain.UserFilter;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.io.Serializable;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
@Builder
public record RetrieveUserRequest(
    @Nullable String email,

    @Nullable String name,

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String createdAtStart,

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String createdAtEnd,

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String updatedAtStart,

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String updatedAtEnd) implements Serializable {
    public UserFilter toDomain() {
        var builder = UserFilter.builder()
            .email(email)
            .name(name);

        if (createdAtStart != null && createdAtEnd != null) {
            var createdAtRange = LocalDateTimeUtil.parseRangeFromString(
                "yyyy-MM-dd",
                createdAtStart,
                createdAtEnd);
            builder.createdAtStart(createdAtRange.startAt());
            builder.createdAtEnd(createdAtRange.endAt());
        }

        if (updatedAtStart != null && updatedAtEnd != null) {
            var updatedAtRange = LocalDateTimeUtil.parseRangeFromString(
                "yyyy-MM-dd",
                updatedAtStart,
                updatedAtEnd);
            builder.updatedAtStart(updatedAtRange.startAt());
            builder.updatedAtEnd(updatedAtRange.endAt());
        }

        return builder.build();
    }
}
