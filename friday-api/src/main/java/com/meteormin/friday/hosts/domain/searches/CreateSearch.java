package com.meteormin.friday.hosts.domain.searches;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateSearch(
    Long hostId,
    Long userId,
    String queryKey,
    String query,
    String description,
    boolean publish,
    List<Long> images
) {
}
