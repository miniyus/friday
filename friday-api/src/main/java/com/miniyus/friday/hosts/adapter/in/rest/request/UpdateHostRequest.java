package com.miniyus.friday.hosts.adapter.in.rest.request;

import com.miniyus.friday.hosts.domain.Host;
import com.miniyus.friday.hosts.domain.HostIds;
import com.miniyus.friday.hosts.domain.PatchHost;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * DTO for {@link Host}
 */
@Builder
public record UpdateHostRequest(
    Long id,
    @NotBlank(message = "validation.host.host.notBlank") String host,
    @NotBlank(message = "validation.host.summary.notBlank") String summary,
    @NotBlank(message = "validation.host.description.notBlank") String description,
    @NotBlank(message = "validation.host.path.notBlank") String path,
    @NotBlank(message = "validation.host.publish.notBlank") boolean publish) {
    public PatchHost toDomain(Long id, Long userId) {
        var ids = HostIds.builder()
                    .id(id)
                    .userId(userId)
                    .build();

        return PatchHost.builder()
            .ids(ids)
            .host(host)
            .summary(summary)
            .description(description)
            .path(path)
            .publish(publish)
            .build();
    }
}
