package com.miniyus.friday.api.hosts.application.port.in.usecase;

import com.miniyus.friday.api.hosts.domain.Host;
import lombok.Builder;

/**
 * DTO for {@link Host}
 */
@Builder
public record UpdateHostRequest(
        String host,
        String summary,
        String description,
        String path,
        boolean publish) {
}
