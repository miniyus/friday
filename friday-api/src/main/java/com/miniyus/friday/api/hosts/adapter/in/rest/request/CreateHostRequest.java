package com.miniyus.friday.api.hosts.adapter.in.rest.request;

import com.miniyus.friday.api.hosts.application.port.in.usecase.CreateHostCommand;
import com.miniyus.friday.api.hosts.domain.Host;

import java.io.Serializable;

/**
 * DTO for {@link Host}
 */
public record CreateHostRequest(
    String host,
    String summary,
    String description,
    String path,
    boolean publish)
    implements Serializable {
    public CreateHostCommand toCommand() {
        return CreateHostCommand.builder()
            .host(host)
            .summary(summary)
            .description(description)
            .path(path)
            .publish(publish)
            .build();
    }
}
