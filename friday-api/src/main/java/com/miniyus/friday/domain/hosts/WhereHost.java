package com.miniyus.friday.domain.hosts;

import lombok.Builder;

@Builder
public record WhereHost(
    String host,
    Long userId
) {
}
