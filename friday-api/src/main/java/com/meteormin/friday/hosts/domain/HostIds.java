package com.meteormin.friday.hosts.domain;

import lombok.Builder;

@Builder
public record HostIds(
    Long id,
    Long userId
) {

}
