package com.miniyus.friday.hosts.domain;

import lombok.Builder;

@Builder
public record WherePublish(
    boolean publish,
    Long userId
) {
}
