package com.miniyus.friday.hosts.domain.searches;

import lombok.Builder;

@Builder
public record SearchFilter(
    String queryKey,
    String query,
    boolean publish,
    Long hostId
) {
}
