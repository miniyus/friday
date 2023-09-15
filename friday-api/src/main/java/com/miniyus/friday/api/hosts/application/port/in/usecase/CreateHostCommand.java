package com.miniyus.friday.api.hosts.application.port.in.usecase;

import lombok.Builder;

/**
 * DTO for {@link com.miniyus.friday.infrastructure.jpa.entities.HostEntity}
 */
@Builder
public record CreateHostCommand(
        String host,
        String summary,
        String description,
        String path,
        boolean publish) {

}
