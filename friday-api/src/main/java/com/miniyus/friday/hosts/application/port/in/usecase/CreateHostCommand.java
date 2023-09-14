package com.miniyus.friday.hosts.application.port.in.usecase;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

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
