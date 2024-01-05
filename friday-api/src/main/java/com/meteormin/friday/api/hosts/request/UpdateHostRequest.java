package com.meteormin.friday.api.hosts.request;

import com.meteormin.friday.hosts.domain.Host;
import com.meteormin.friday.hosts.domain.HostIds;
import com.meteormin.friday.hosts.domain.PatchHost;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * DTO for {@link Host}
 */
@Builder
public record UpdateHostRequest(
    @NotBlank(message = "validation.host.host.notBlank")
    @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
    JsonNullable<String> host,

    @NotBlank(message = "validation.host.summary.notBlank")
    @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
    JsonNullable<String> summary,

    @NotBlank(message = "validation.host.description.notBlank")
    @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
    JsonNullable<String> description,

    @NotBlank(message = "validation.host.path.notBlank")
    @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
    JsonNullable<String> path,

    @NotBlank(message = "validation.host.publish.notBlank")
    @Schema(type = "boolean", requiredMode = Schema.RequiredMode.REQUIRED)
    JsonNullable<Boolean> publish) {

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
