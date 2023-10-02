package com.miniyus.friday.domain.hosts.searches;

import lombok.Builder;

@Builder
public record SearchIds(
    Long id,
    Long hostId,
    Long userId
) {
}
