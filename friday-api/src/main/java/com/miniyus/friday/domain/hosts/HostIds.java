package com.miniyus.friday.domain.hosts;

import lombok.Builder;

@Builder
public record HostIds(
    Long id,
    Long userId
) {

}
