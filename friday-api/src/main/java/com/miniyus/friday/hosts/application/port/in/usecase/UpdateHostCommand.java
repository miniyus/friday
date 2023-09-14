package com.miniyus.friday.hosts.application.port.in.usecase;

import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link com.miniyus.friday.hosts.domain.Host}
 */
@Builder
public record UpdateHostCommand(
    Long id,
    String host,
    String summary,
    String description,
    String path,
    boolean publish) {
}
