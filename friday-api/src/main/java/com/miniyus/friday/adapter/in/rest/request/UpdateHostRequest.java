package com.miniyus.friday.adapter.in.rest.request;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.HostIds;
import com.miniyus.friday.domain.hosts.UpdateHost;
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
    public UpdateHost toDomain(Long id, Long userId) {
        var ids = HostIds.builder()
                    .id(id)
                    .userId(userId)
                    .build();

        return UpdateHost.builder()
            .ids(ids)
            .host(host)
            .summary(summary)
            .description(description)
            .path(path)
            .publish(publish)
            .build();
    }
}
