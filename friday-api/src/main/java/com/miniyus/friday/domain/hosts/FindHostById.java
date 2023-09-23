package com.miniyus.friday.domain.hosts;

import lombok.Builder;

@Builder
public record FindHostById(
    Long id,
    Long userId
) {

}
