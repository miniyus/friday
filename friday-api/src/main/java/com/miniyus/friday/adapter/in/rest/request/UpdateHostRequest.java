package com.miniyus.friday.adapter.in.rest.request;

import com.miniyus.friday.domain.hosts.Host;
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
