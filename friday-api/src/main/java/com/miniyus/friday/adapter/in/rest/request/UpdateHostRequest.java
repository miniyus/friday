package com.miniyus.friday.adapter.in.rest.request;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.UpdateHost;
import lombok.Builder;

/**
 * DTO for {@link Host}
 */
@Builder
public record UpdateHostRequest(
    Long id,
    String host,
    String summary,
    String description,
    String path,
    boolean publish) {
    public UpdateHost fromDomain(Long userId) {
        return UpdateHost.builder()
            .id(id)
            .host(host)
            .summary(summary)
            .description(description)
            .path(path)
            .publish(publish)
            .userId(userId)
            .build();
    }
}
