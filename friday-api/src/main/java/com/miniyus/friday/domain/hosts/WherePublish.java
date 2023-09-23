package com.miniyus.friday.domain.hosts;

import lombok.Builder;

@Builder
public record WherePublish(
    boolean publish,
    Long userId
) {
}
