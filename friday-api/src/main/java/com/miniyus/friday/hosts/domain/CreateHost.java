package com.miniyus.friday.hosts.domain;

import lombok.Builder;

@Builder
public record CreateHost(
    String host,
    String summary,
    String description,
    String path,
    boolean publish,
    Long userId
) {
}
