package com.miniyus.friday.domain.hosts.searches;

import lombok.Builder;

@Builder
public record SearchFilter(
    String queryKey,
    String query,
    boolean publish,
    Long hostId
) {
}
