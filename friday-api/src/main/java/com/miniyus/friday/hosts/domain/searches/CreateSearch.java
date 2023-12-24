package com.miniyus.friday.hosts.domain.searches;

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
