package com.meteormin.friday.hosts.domain.searches;

import lombok.Builder;

@Builder
public record SearchIds(
    Long id,
    Long hostId,
    Long userId
) {
}
