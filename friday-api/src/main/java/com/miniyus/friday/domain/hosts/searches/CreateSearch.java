package com.miniyus.friday.domain.hosts.searches;

import lombok.Builder;

@Builder
public record CreateSearch(
    Long hostId,
    Long userId,
    String queryKey,
    String query,
    String description,
    boolean publish,
    SearchImage searchImage
) {
}
