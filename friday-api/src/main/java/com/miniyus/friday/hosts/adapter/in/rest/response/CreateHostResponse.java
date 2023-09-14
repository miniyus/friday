package com.miniyus.friday.hosts.adapter.in.rest.response;

import com.miniyus.friday.hosts.domain.Host;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.miniyus.friday.hosts.domain.Host}
 */
@Builder
public record CreateHostResponse(Long id, String host, String summary, String description,
                                 String path, boolean publish, LocalDateTime createdAt,
                                 LocalDateTime updatedAt)
    implements Serializable {
    public static CreateHostResponse fromDomain(Host host) {
        return CreateHostResponse.builder()
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
