package com.miniyus.friday.adapter.in.rest.request;

import com.miniyus.friday.domain.hosts.CreateHost;
import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * DTO for {@link HostEntity}
 */
@Builder
public record CreateHostRequest(
    @NotBlank(message = "validation.host.host.notBlank")
    String host,
    @NotBlank(message = "validation.host.summary.notBlank")
    String summary,
    @NotBlank(message = "validation.host.description.notBlank")
    String description,
    @NotBlank(message = "validation.host.path.notBlank")
    String path,
    @NotBlank(message = "validation.host.publish.notBlank")
    boolean publish
) {
    public CreateHost toDomain(Long userId) {
        return CreateHost.builder()
            .host(host)
            .summary(summary)
            .description(description)
            .path(path)
            .publish(publish)
            .userId(userId)
            .build();
    }
}
