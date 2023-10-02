package com.miniyus.friday.domain.hosts;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record HostFilter(
    String host,
    String summary,
    String path,
    String description,
    LocalDateTime createdAtStart,
    LocalDateTime createdAtEnd,
    LocalDateTime updatedAtStart,
    LocalDateTime updatedAtEnd,
    Long userId
) {
    public boolean isEmpty() {
        return host == null && summary == null && path == null && description == null
            && createdAtStart == null && updatedAtStart == null
            && createdAtEnd == null && updatedAtEnd == null && userId == null;
    }
}

