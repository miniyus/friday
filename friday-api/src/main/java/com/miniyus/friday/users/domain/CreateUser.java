package com.miniyus.friday.users.domain;

import lombok.Builder;

@Builder
public record CreateUser(
    String email,
    String password,
    String name,
    UserRole role
) {
}
