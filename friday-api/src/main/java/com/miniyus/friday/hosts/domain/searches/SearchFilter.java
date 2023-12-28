package com.miniyus.friday.hosts.domain.searches;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record SearchFilter(
    Long userId,
    String queryKey,
    String query,
    Boolean publish,
    Long hostId,
    Pageable pageable
) {
}
