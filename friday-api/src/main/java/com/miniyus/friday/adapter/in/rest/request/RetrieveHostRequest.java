package com.miniyus.friday.adapter.in.rest.request;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.common.pagination.PageRequest;
import com.miniyus.friday.domain.hosts.HostFilter;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

/**
 * DTO for {@link Host}
 */

@Builder
@Getter
public class RetrieveHostRequest extends PageRequest {
    @Nullable
    String host;
    @Nullable
    String summary;
    @Nullable
    String description;
    @Nullable
    String path;
    @Nullable
    LocalDateTime createdAtStart;
    @Nullable
    LocalDateTime createdAtEnd;
    @Nullable
    LocalDateTime updatedAtStart;
    @Nullable
    LocalDateTime updatedAtEnd;
    @Nullable
    Pageable pageable;

    public Pageable getPageable() {
        return toPageable();
    }

    public HostFilter toDomain(Long userId) {
        return HostFilter.builder()
            .userId(userId)
            .summary(summary)
            .description(description)
            .path(path)
            .createdAtStart(createdAtStart)
            .createdAtEnd(createdAtEnd)
            .updatedAtStart(updatedAtStart)
            .updatedAtEnd(updatedAtEnd)
            .build();
    }

    public static RetrieveHostRequest create() {
        return RetrieveHostRequest.builder()
            .pageable(
                org.springframework.data.domain.PageRequest.of(
                    0,
                    20,
                    Sort.Direction.DESC,
                    "createdAt")
            ).build();
    }

    public static RetrieveHostRequest create(int pageNumber, int pageSize, String sortDirection,
        String sortField) {
        return RetrieveHostRequest.builder()
            .pageable(
                org.springframework.data.domain.PageRequest.of(
                    pageNumber,
                    pageSize,
                    Sort.Direction.fromString(sortDirection),
                    sortField)
            ).build();
    }
}

