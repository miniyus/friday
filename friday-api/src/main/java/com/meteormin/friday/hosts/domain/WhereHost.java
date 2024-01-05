package com.meteormin.friday.hosts.domain;

import lombok.Builder;

@Builder
public record WhereHost(
    String host,
    Long userId
) {
}
