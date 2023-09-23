package com.miniyus.friday.domain.users;

import lombok.Builder;

@Builder
public record UpdateUser(
    Long id,
    String name,
    String role
) {
}
