package com.miniyus.friday.api.hosts.application.port.in;

import com.miniyus.friday.api.hosts.domain.Host;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Host}
 */
@Builder
public record HostResource(
    Long id,
    String host,
    String summary,
    String description,
    String path,
    boolean publish,
    LocalDateTime createdAt,
    LocalDateTime updatedAt)
    implements Serializable {
    public static HostResource fromDomain(Host host) {
        return HostResource.builder()
            .id(host.getId())
            .host(host.getHost())
            .summary(host.getSummary())
            .description(host.getDescription())
            .path(host.getPath())
            .publish(host.isPublish())
            .createdAt(host.getCreatedAt())
            .updatedAt(host.getUpdatedAt())
            .build();
    }
}
