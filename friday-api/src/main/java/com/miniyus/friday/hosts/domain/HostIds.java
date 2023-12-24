package com.miniyus.friday.hosts.domain;

import lombok.Builder;

@Builder
public record HostIds(
    Long id,
    Long userId
) {

}
