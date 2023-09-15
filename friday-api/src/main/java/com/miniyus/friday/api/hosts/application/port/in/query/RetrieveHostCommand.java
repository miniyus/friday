package com.miniyus.friday.api.hosts.application.port.in.query;

import com.miniyus.friday.api.hosts.domain.Host;
import lombok.Builder;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Host}
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
