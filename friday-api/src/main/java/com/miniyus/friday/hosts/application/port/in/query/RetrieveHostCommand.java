package com.miniyus.friday.hosts.application.port.in.query;

import lombok.Builder;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.miniyus.friday.hosts.domain.Host}
 */
@Builder
public record RetrieveHostCommand(
    String host,
    String summary,
    String description,
    String path,
    LocalDateTime createdAtStart,
    LocalDateTime createdAtEnd,
    LocalDateTime updatedAtStart,
    LocalDateTime updatedAtEnd,
    Pageable pageable) {
}
