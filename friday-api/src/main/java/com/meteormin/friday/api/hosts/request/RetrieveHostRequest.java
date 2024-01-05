package com.meteormin.friday.api.hosts.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meteormin.friday.common.pagination.PageRequest;
import com.meteormin.friday.hosts.domain.Host;
import com.meteormin.friday.hosts.domain.HostFilter;
import lombok.Builder;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import static com.meteormin.friday.common.util.LocalDateTimeUtil.parseRangeFromString;

/**
 * DTO for {@link Host}
 */
@Builder
public record RetrieveHostRequest(
    @Nullable String host,
    @Nullable String summary,
    @Nullable String description,
    @Nullable String path,

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
    String updatedAtEnd) {

    public HostFilter toDomain(Long userId, Pageable pageable) {
        var builder = HostFilter.builder()
            .userId(userId)
            .summary(summary)
            .description(description)
            .path(path)
            .pageable(PageRequest.of(pageable));

        if (createdAtStart != null && createdAtEnd != null) {
            var createdAtRange = parseRangeFromString("yyyy-MM-dd",
                createdAtStart,
                createdAtEnd);
            builder.createdAtStart(createdAtRange.startAt())
                .createdAtEnd(createdAtRange.endAt());
        }

        if (updatedAtStart != null && updatedAtEnd != null) {
            var updatedAtRange = parseRangeFromString("yyyy-MM-dd",
                updatedAtStart,
                updatedAtEnd);
            builder.updatedAtStart(updatedAtRange.startAt())
                .updatedAtEnd(updatedAtRange.endAt());
        }

        return builder.build();
    }
}

