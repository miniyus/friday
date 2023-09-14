package com.miniyus.friday.hosts.adapter.in.rest.request;

import com.miniyus.friday.hosts.application.port.in.usecase.CreateHostCommand;

import java.io.Serializable;

/**
 * DTO for {@link com.miniyus.friday.hosts.domain.Host}
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
